package it.marcoberri.mbmeteo.chart;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleEdge;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 
 * Sample -url http://meteo.marcoberri.it/ -f /tmp/
 * 
 * @author berri
 *
 */
public class Main {

	public enum Filter {
		WEEK("7"), DAY("24"), YEAR("365"), MONTH("30");

		private final String code;

		Filter(String code) {
			this.code = code;
		};

		public String getCode() {
			return this.code;
		}

	}

	private static int fontSize = 12;
	private static String fontType = "Helvetica";
	private static Color color = new Color(136, 177, 249);

	public SimpleDateFormat sdfDay = new SimpleDateFormat("dd-MM-yyyy");
	public SimpleDateFormat sdfHourDay = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	public SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");

	public static void main(String[] args) {
		if (ConfigurationHelper.getProperties() == null) {
			System.out.println("no properties loaded");
			System.exit(1);
		}

		Options options = new Options();
		options.addOption("url", true, "url to call for data");
		options.addOption("f", true, "target file name");
		options.addOption("h", "help", false, "this help");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
			if (cmd.hasOption("f")) {

				final String url = cmd.getOptionValue("url");
				final String file = cmd.getOptionValue("f");

				Main o = new Main();
				o.generateHT(url, Filter.DAY.code, file, "last 24 Hour", 650, 460);
				o.generateP(url, Filter.DAY.code, file, "last 24 Hour", 600, 500);
				o.generateR(url, Filter.DAY.code, file, "last 24 Hour", 1200, 300);
				o.generateWC(url, Filter.DAY.code, file, "last 24 Hour", 600, 500);

				o.generateT(url, Filter.WEEK.code, file, "last 7 Day", 1000, 400);
				o.generateT(url, Filter.MONTH.code, file, "last 30 Day", 1000, 400);
				o.generateT(url, Filter.YEAR.code, file, "last Year", 1000, 400);

				o.generateP(url, Filter.WEEK.code, file, "last 7 Day", 1000, 400);
				o.generateP(url, Filter.MONTH.code, file, "last 30 Day", 1000, 400);
				o.generateP(url, Filter.YEAR.code, file, "last Year", 1000, 400);

				o.generateH(url, Filter.WEEK.code, file, "last 7 Day", 1000, 400);
				o.generateH(url, Filter.MONTH.code, file, "last 30 Day", 1000, 400);
				o.generateH(url, Filter.YEAR.code, file, "last Year", 1000, 400);

				o.generateR(url, Filter.WEEK.code, file, "last 7 Day", 1000, 400);
				o.generateR(url, Filter.MONTH.code, file, "last 30 Day", 1000, 400);
				o.generateR(url, Filter.YEAR.code, file, "last Year", 1000, 400);

				o.generateWC(url, Filter.WEEK.code, file, "last 7 Day", 1000, 400);
				o.generateWC(url, Filter.MONTH.code, file, "last 30 Day", 1000, 400);
				o.generateWC(url, Filter.YEAR.code, file, "last Year", 1000, 400);

			} else {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -jar <jar_name>" + "\nVersion:" + ConfigurationHelper.getProperties().getProperty("app.version") + "\nbuild: " + ConfigurationHelper.getProperties().getProperty("app.build"), options);
			}

		} catch (final ParseException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public void generateT(String url, String type, String folder, String titleChart, int width, int height) {

		try {

			final TimeSeriesCollection dataset1 = new TimeSeriesCollection();
			dataset1.addSeries(getTimeSeries("°C", url, "T/", type, "T1"));

			final JFreeChart chart = ChartFactory.createTimeSeriesChart("Temperature °C " + titleChart, "Date", "°C", dataset1, true, true, false);
			final XYPlot plot = chart.getXYPlot();
			final DateAxis axis = setAxis((DateAxis) plot.getDomainAxis(), type);

			chart.addSubtitle(getLeggendDate());

			File lineChart = new File(folder + "/t" + type + ".jpg");

			plot.getRangeAxis().setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			axis.setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			plot.setBackgroundPaint(color);

			ChartUtilities.saveChartAsJPEG(lineChart, chart, width, height);

		} catch (final ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void generateP(String url, String type, String folder, String titleChart, int width, int height) {

		try {

			final TimeSeriesCollection dataset1 = new TimeSeriesCollection();
			dataset1.addSeries(getTimeSeries("mBar", url, "PRESS/", type, "PRESS"));

			final JFreeChart chart = ChartFactory.createTimeSeriesChart("Pressure mBar " + titleChart, "Date", "mBar", dataset1, true, true, false);
			final XYPlot plot = chart.getXYPlot();
			final DateAxis axis = setAxis((DateAxis) plot.getDomainAxis(), type);

			chart.addSubtitle(getLeggendDate());

			File lineChart = new File(folder + "/p" + type + ".jpg");

			plot.getRangeAxis().setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			axis.setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			plot.setBackgroundPaint(color);

			ChartUtilities.saveChartAsJPEG(lineChart, chart, width, height);

		} catch (final ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void generateH(String url, String type, String folder, String titleChart, int width, int height) {

		try {

			final TimeSeriesCollection dataset1 = new TimeSeriesCollection();
			dataset1.addSeries(getTimeSeries("%", url, "H/", type, "H1"));

			final JFreeChart chart = ChartFactory.createTimeSeriesChart("Humidity % " + titleChart, "Date", "%", dataset1, true, true, false);
			final XYPlot plot = chart.getXYPlot();
			final DateAxis axis = setAxis((DateAxis) plot.getDomainAxis(), type);

			chart.addSubtitle(getLeggendDate());

			File lineChart = new File(folder + "/h" + type + ".jpg");

			plot.getRangeAxis().setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			axis.setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			plot.setBackgroundPaint(color);

			ChartUtilities.saveChartAsJPEG(lineChart, chart, width, height);

		} catch (final ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void generateWC(String url, String type, String folder, String titleChart, int width, int height) {

		try {

			final TimeSeriesCollection dataset1 = new TimeSeriesCollection();
			dataset1.addSeries(getTimeSeries("T°", url, "WC/", type, "WC"));

			final JFreeChart chart = ChartFactory.createTimeSeriesChart("Wind Chill T° " + titleChart, "Date", "T°", dataset1, true, true, false);

			final XYPlot plot = chart.getXYPlot();

			final DateAxis axis = setAxis((DateAxis) plot.getDomainAxis(), type);

			chart.addSubtitle(getLeggendDate());

			plot.getRangeAxis().setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			axis.setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			plot.setBackgroundPaint(color);
			File lineChart = new File(folder + "/wc" + type + ".jpg");

			ChartUtilities.saveChartAsJPEG(lineChart, chart, width, height);

		} catch (final ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void generateR(String url, String type, String folder, String titleChart, int width, int height) {

		try {

			final TimeSeriesCollection dataset1 = new TimeSeriesCollection();
			dataset1.addSeries(getTimeSeries("mm", url, "RC/", type, "RC",-1));

			final JFreeChart chart = ChartFactory.createTimeSeriesChart("Rain mm " + titleChart, "Date", "mm", dataset1, true, true, false);

			final XYPlot plot = chart.getXYPlot();

			final DateAxis axis = setAxis((DateAxis) plot.getDomainAxis(), type);

			chart.addSubtitle(getLeggendDate());

			plot.getRangeAxis().setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			axis.setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			plot.setBackgroundPaint(color);

			File lineChart = new File(folder + "/rc" + type + ".jpg");

			ChartUtilities.saveChartAsJPEG(lineChart, chart, width, height);

		} catch (final ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void generateHT(String url, String type, String folder, String title, int width, int height) {

		try {

			final TimeSeriesCollection dataset1 = new TimeSeriesCollection();
			dataset1.setDomainIsPointsInTime(true);
			dataset1.addSeries(getTimeSeries("°C", url, "T/", type, "T1"));

			final TimeSeriesCollection dataset2 = new TimeSeriesCollection();
			dataset2.addSeries(getTimeSeries("H%°", url, "H/", type, "H1"));

			final JFreeChart chart = ChartFactory.createTimeSeriesChart("Tempertaure / Humidity " + title, "Date", "Tempertature °C", dataset1, true, true, false);

			final XYPlot plot = chart.getXYPlot();
			final NumberAxis axis2 = new NumberAxis("Humidity %");
			axis2.setAutoRangeIncludesZero(false);
			plot.setRangeAxis(1, axis2);
			plot.setDataset(1, dataset2);
			plot.mapDatasetToRangeAxis(1, 1);
			final XYItemRenderer renderer = plot.getRenderer();
			if (renderer instanceof StandardXYItemRenderer) {
				final StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
				rr.setShapesFilled(true);
			}

			final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
			renderer2.setSeriesPaint(0, Color.blue);

			plot.setRenderer(1, renderer2);

			final DateAxis axis = setAxis((DateAxis) plot.getDomainAxis(), type);

			chart.addSubtitle(getLeggendDate());

			plot.getRangeAxis().setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			axis.setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			plot.setBackgroundPaint(color);

			File lineChart = new File(folder + "/th" + type + ".jpg");
			ChartUtilities.saveChartAsJPEG(lineChart, chart, width, height);

		} catch (final ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private DateAxis setAxis(DateAxis domainAxis, String type) {

		if (type.equalsIgnoreCase(Filter.DAY.code))
			domainAxis.setDateFormatOverride(sdfHour);
		else if (type.equalsIgnoreCase(Filter.WEEK.code))
			domainAxis.setDateFormatOverride(sdfHourDay);
		else
			domainAxis.setDateFormatOverride(sdfDay);
		domainAxis.setVerticalTickLabels(true);

		return domainAxis;
	}

	public TextTitle getLeggendDate() {

		final TextTitle legendText = new TextTitle(sdfHourDay.format(new Date()));
		legendText.setPosition(RectangleEdge.RIGHT);
		legendText.setFont(new java.awt.Font("SansSerif", java.awt.Font.ITALIC, 12));
		return legendText;

	}

	public TimeSeries getTimeSeries(String tseries, String url, String baseUrl, String type, String field) throws ClientProtocolException, IOException {
		return getTimeSeries(tseries, url, baseUrl, type, field, -1000);
	}

	public TimeSeries getTimeSeries(String tseries, String url, String baseUrl, String type, String field, int gap) throws ClientProtocolException, IOException {

		final TimeSeries serie1 = new TimeSeries(tseries);

		System.out.println("call " + url + baseUrl + type);

		String s = HttpHelper.getData(url + baseUrl + type);
		Gson gson = new Gson();
		final JsonArray jsonArray = gson.fromJson(s, JsonArray.class);

		final HashMap<Date, Long> m = new HashMap<Date, Long>();

		for (JsonElement e : jsonArray) {

			JsonObject o = e.getAsJsonObject();
			if (o.get(field) == null || o.get(field).isJsonNull()) {
				continue;
			}

			long limit = 1431730684000l;

			if (o.get("ts").getAsLong() < limit)
				continue;

			
			m.put(new Date(o.get("ts").getAsLong()), o.get(field).getAsLong() + (gap != -1000 ? gap :0));
		}

		for (Map.Entry<Date, Long> entry : m.entrySet()) {
			serie1.add(new FixedMillisecond(entry.getKey()), entry.getValue());
		}
		System.out.println("tot ele :" + serie1.getItems().size());
		return serie1;
	}
}
