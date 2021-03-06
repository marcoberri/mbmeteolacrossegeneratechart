package it.marcoberri.mbmeteo.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

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
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.ui.RectangleEdge;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

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
		options.addOption("f", true, "target folder name");

		options.addOption("d", false, "generation day chart");
		options.addOption("w", false, "generation week chart");
		options.addOption("y", false, "generation year chart");
		options.addOption("m", false, "generation month chart");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
			if (cmd.hasOption("f")) {

				final String url = cmd.getOptionValue("url");
				final String file = cmd.getOptionValue("f");

				Main o = new Main();
				boolean notting = true;

				if (cmd.hasOption("d")) {
				//	o.dayReport();
				//	o.generateT(url, Filter.DAY.code, file, "last 24 Hour", 600, 460);
					//o.generateH(url, Filter.DAY.code, file, "last 24 Hour", 600, 460);
					// o.generateHT(url, Filter.DAY.code, file, "last 24 Hour",
					// 600, 460);
					// o.generateP(url, Filter.DAY.code, file, "last 24 Hour",
					// 600, 460);
					// o.generateR(url, Filter.DAY.code, file, "last 24 Hour",
					// 600, 460);
					// o.generateWC(url, Filter.DAY.code, file, "last 24 Hour",
					// 600, 460);
					// o.generateWDWS(url, Filter.DAY.code, file, "last 24
					// Hour", 600, 460);

					notting = false;
				}

				if (cmd.hasOption("w")) {
					o.generateT(url, Filter.WEEK.code, file, "last 7 Day", 800, 400);
					//o.generateH(url, Filter.WEEK.code, file, "last 7 Day", 800, 400);

					// o.generateP(url,Filter.WEEK.code, file, "last 7 Day",
					// 800, 400);

					// o.generateR(url, Filter.WEEK.code, file, "last 7 Day",
					// 800, 400);
					// o.generateWC(url, Filter.WEEK.code, file, "last 7 Day",
					// 800, 400);
					// o.generateWDWS(url, Filter.WEEK.code, file, "last 7
					// Hour", 400, 400);
					notting = false;
				}

				if (cmd.hasOption("m")) {
					// o.generateT(url, Filter.MONTH.code, file, "last 30 Day", 800, 400);
					// o.generateP(url, Filter.MONTH.code, file, "last 30 Day",
					// 800, 400);
					// o.generateH(url, Filter.MONTH.code, file, "last 30 Day",
					// 800, 400);
					// o.generateR(url, Filter.MONTH.code, file, "last 30 Day",
					// 800, 400);
					// o.generateWC(url, Filter.MONTH.code, file, "last 30 Day",
					// 800, 400);
					// o.generateWDWS(url, Filter.MONTH.code, file, "last 30
					// Hour", 400, 400);
					notting = false;
				}

				if (cmd.hasOption("y")) {
					// o.generateT(url, Filter.YEAR.code, file, "last Year",
					// 800, 400);
					// o.generateP(url, Filter.YEAR.code, file, "last Year",
					// 800, 400);
					// o.generateH(url, Filter.YEAR.code, file, "last Year",
					// 800, 400);
					// o.generateR(url, Filter.YEAR.code, file, "last Year",
					// 800, 400);
					// o.generateWC(url, Filter.YEAR.code, file, "last Year",
					// 800, 400);
					// o.generateWDWS(url, Filter.MONTH.code, file, "last Year",
					// 400, 400);
					notting = false;
				}

				if (notting) {
					System.out.println("Notting to do!!!");
				}

			} else {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -jar <jar_name>" + "\nVersion:"
						+ ConfigurationHelper.getProperties().getProperty("app.version") + "\nbuild: "
						+ ConfigurationHelper.getProperties().getProperty("app.build"), options);
			}

		} catch (final ParseException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	private void dayReport() {
		try {

			String s = HttpHelper.getData("http://meteo.marcoberri.it/T/24");

			System.out.println(s);
			
			
			Gson gson = new Gson();
			final JsonArray jsonArray = gson.fromJson(s, JsonArray.class);

			ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			for (JsonElement e : jsonArray) {

				JsonObject o = e.getAsJsonObject();
				if (o.get("T1") == null || o.get("T1").isJsonNull()) {
					continue;
				}

				long limit = 1431730684000l;

				if (o.get("ts").getAsLong() < limit)
					continue;
				final HashMap<String, Object> m = new HashMap<String, Object>();
				m.put("ts", new Date(o.get("ts").getAsLong()));
				m.put("T1", o.get("T1").getAsFloat());
				data.add(m);
			}

			InputStream inputStream = ConfigurationHelper.class.getResourceAsStream("/dayReport.jrxml");
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(data);

			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), beanColDataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "/tmp/test_jasper.pdf");

		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JRException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void generateWDWS(String url, String type, String folder, String titleChart, int width, int height) {

		try {
			String s = HttpHelper.getData(url + "/WDWS/" + type);

			Gson gson = new Gson();
			final JsonArray jsonArray = gson.fromJson(s, JsonArray.class);

			Map<Double, Double> distinctValue = new TreeMap<Double, Double>();

			for (JsonElement e : jsonArray) {
				JsonObject o = e.getAsJsonObject();
				if (o.get("WD") == null || o.get("WD").isJsonNull()) {
					continue;
				}
				long limit = 1431730684000l;

				if (o.get("ts").getAsLong() < limit)
					continue;

				Double d = new Double(o.get("WD").getAsLong() * 22.5);
				Double n = new Double(o.get("WS").getAsDouble());

				if (distinctValue.containsKey(d)) {
					Double temp = distinctValue.get(d);
					temp += n;
					distinctValue.put(d, temp);
				} else {
					distinctValue.put(d, n);
				}

			}

			DefaultCategoryDataset categorydataset = new DefaultCategoryDataset();

			for (Map.Entry<Double, Double> entry : distinctValue.entrySet()) {

				String lbl = "°" + entry.getKey();
				if (entry.getKey() == 0)
					lbl = "North";
				else if (entry.getKey() == 180)
					lbl = "South";
				else if (entry.getKey() == 90)
					lbl = "West";
				else if (entry.getKey() == 270)
					lbl = "Ovest";
				else if (entry.getKey() == 315)
					lbl = "NO";
				else if (entry.getKey() == 225)
					lbl = "SO";
				else if (entry.getKey() == 135)
					lbl = "SW";
				else if (entry.getKey() == 45)
					lbl = "NW";

				categorydataset.addValue(entry.getValue(), entry.getKey(), lbl);

			}

			final Color bckColor1 = Color.decode("#4282CE"); // Light blue
			final Color bckColor2 = Color.decode("#9BC1FF"); // Dark blue

			final Color axisColor = Color.decode("#DD0010"); // Red

			final SpiderWebPlot plot = new SpiderWebPlot(categorydataset);
			final Paint p = new GradientPaint(0, 0, bckColor1, 0, 0, bckColor2);

			plot.setSeriesPaint(0, p);
			plot.setSeriesOutlineStroke(new BasicStroke(5));
			plot.setAxisLinePaint(axisColor);
			plot.setLabelPaint(new GradientPaint(1.0f, 2.0f, Color.red, 3.0f, 4.0f, Color.blue));

			final JFreeChart chart = new JFreeChart("Wind Direction/Speed", TextTitle.DEFAULT_FONT, plot, false);
			chart.addSubtitle(getLeggendDate());
			final File lineChart = new File(folder + "/wdws" + type + ".jpg");

			ChartUtilities.saveChartAsJPEG(lineChart, chart, width, height);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);

		}

	}

	public void generateT(String url, String type, String folder, String titleChart, int width, int height) {

		try {

			final TimeSeriesCollection dataset1 = new TimeSeriesCollection();
			TimeSeries act = getTimeSeries("°C", url, "T/", type, "T1");
			dataset1.addSeries(act);

			final TimeSeriesDataItem dataFirst = act.getDataItem(0);
			final RegularTimePeriod starttime = dataFirst.getPeriod();

			final JFreeChart chart = ChartFactory.createTimeSeriesChart("Temperature °C " + titleChart, "Date", "°C",
					dataset1, true, true, false);

			final XYPlot plot = chart.getXYPlot();

			final StandardXYItemRenderer renderer0 = new StandardXYItemRenderer();
			renderer0.setSeriesPaint(0, Color.yellow);
			plot.setRenderer(0, renderer0);

			final DateAxis axis = setAxis((DateAxis) plot.getDomainAxis(), type);

			
			String partialField = "Hour";
			
			if(type.equals(Filter.DAY.code))
				partialField = "Hour";
			
			else if(type.equals(Filter.WEEK.code) || type.equals(Filter.MONTH.code))
				partialField = "Day";
			
			final TimeSeriesCollection dataset2 = new TimeSeriesCollection();
			dataset2.addSeries(
					getTimeSeriesMaxMin("°C", url, "T/MaxMin/", type, "T1Min" + partialField, starttime.getFirstMillisecond()));

			plot.setDataset(1, dataset2);

			final StandardXYItemRenderer renderer1 = new StandardXYItemRenderer();
			renderer1.setSeriesPaint(0, Color.blue);
			plot.setRenderer(1, renderer1);

			final TimeSeriesCollection dataset3 = new TimeSeriesCollection();
			dataset3.addSeries(
					getTimeSeriesMaxMin("°C", url, "T/MaxMin/", type, "T1Max" + partialField, starttime.getFirstMillisecond()));

			plot.setDataset(2, dataset3);

			final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
			renderer2.setSeriesPaint(0, Color.red);
			plot.setRenderer(2, renderer2);

			chart.addSubtitle(getLeggendDate());

			File lineChart = new File(folder + "/t" + type + ".jpg");

			plot.getRangeAxis().setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			axis.setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			plot.setBackgroundPaint(color);
			chart.removeLegend();

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

			final JFreeChart chart = ChartFactory.createTimeSeriesChart("Pressure mBar " + titleChart, "Date", "mBar",
					dataset1, true, true, false);
			final XYPlot plot = chart.getXYPlot();
			final DateAxis axis = setAxis((DateAxis) plot.getDomainAxis(), type);

			chart.addSubtitle(getLeggendDate());

			File lineChart = new File(folder + "/p" + type + ".jpg");

			plot.getRangeAxis().setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			axis.setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			plot.setBackgroundPaint(color);

			chart.removeLegend();

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
			TimeSeries act = getTimeSeries("%", url, "H/", type, "H1");
			dataset1.addSeries(act);

			final TimeSeriesDataItem dataFirst = act.getDataItem(0);
			final RegularTimePeriod starttime = dataFirst.getPeriod();

			final JFreeChart chart = ChartFactory.createTimeSeriesChart("Humidity % " + titleChart, "Date", "%",
					dataset1, true, true, false);

			final XYPlot plot = chart.getXYPlot();

			final StandardXYItemRenderer renderer0 = new StandardXYItemRenderer();
			renderer0.setSeriesPaint(0, Color.yellow);
			plot.setRenderer(0, renderer0);

			final DateAxis axis = setAxis((DateAxis) plot.getDomainAxis(), type);

			final TimeSeriesCollection dataset2 = new TimeSeriesCollection();
			dataset2.addSeries(
					getTimeSeriesMaxMin("%", url, "H/MaxMin/", type, "H1MinHour", starttime.getFirstMillisecond()));

			plot.setDataset(1, dataset2);

			final StandardXYItemRenderer renderer1 = new StandardXYItemRenderer();
			renderer1.setSeriesPaint(0, Color.blue);
			plot.setRenderer(1, renderer1);

			final TimeSeriesCollection dataset3 = new TimeSeriesCollection();
			dataset3.addSeries(
					getTimeSeriesMaxMin("%", url, "H/MaxMin/", type, "H1MaxHour", starttime.getFirstMillisecond()));

			plot.setDataset(2, dataset3);

			final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
			renderer2.setSeriesPaint(0, Color.red);
			plot.setRenderer(2, renderer2);

			chart.addSubtitle(getLeggendDate());

			File lineChart = new File(folder + "/h" + type + ".jpg");

			plot.getRangeAxis().setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			axis.setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			plot.setBackgroundPaint(color);
			chart.removeLegend();

			ChartUtilities.saveChartAsJPEG(lineChart, chart, width, height);

			/*
			 * final TimeSeriesCollection dataset1 = new TimeSeriesCollection();
			 * dataset1.addSeries(getTimeSeries("%", url, "H/", type, "H1"));
			 * 
			 * final JFreeChart chart = ChartFactory.createTimeSeriesChart(
			 * "Humidity % " + titleChart, "Date", "%", dataset1, true, true,
			 * false); final XYPlot plot = chart.getXYPlot(); final DateAxis
			 * axis = setAxis((DateAxis) plot.getDomainAxis(), type);
			 * 
			 * chart.addSubtitle(getLeggendDate());
			 * 
			 * File lineChart = new File(folder + "/h" + type + ".jpg");
			 * 
			 * plot.getRangeAxis().setTickLabelFont(new Font(fontType,
			 * Font.PLAIN, fontSize)); axis.setTickLabelFont(new Font(fontType,
			 * Font.PLAIN, fontSize)); plot.setBackgroundPaint(color);
			 * 
			 * chart.removeLegend(); ChartUtilities.saveChartAsJPEG(lineChart,
			 * chart, width, height);
			 */
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

			final JFreeChart chart = ChartFactory.createTimeSeriesChart("Wind Chill T° " + titleChart, "Date", "T°",
					dataset1, true, true, false);

			final XYPlot plot = chart.getXYPlot();

			final DateAxis axis = setAxis((DateAxis) plot.getDomainAxis(), type);

			chart.addSubtitle(getLeggendDate());

			plot.getRangeAxis().setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			axis.setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			plot.setBackgroundPaint(color);
			File lineChart = new File(folder + "/wc" + type + ".jpg");

			chart.removeLegend();
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
			dataset1.addSeries(getTimeSeriesRain("mm", url, "RC/", type, "RC", -1));

			final JFreeChart chart = ChartFactory.createTimeSeriesChart("Rain mm " + titleChart, "Date", "mm", dataset1,
					true, true, false);

			final XYPlot plot = chart.getXYPlot();

			final DateAxis axis = setAxis((DateAxis) plot.getDomainAxis(), type);

			chart.addSubtitle(getLeggendDate());

			plot.getRangeAxis().setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			axis.setTickLabelFont(new Font(fontType, Font.PLAIN, fontSize));
			plot.setBackgroundPaint(color);

			File lineChart = new File(folder + "/rc" + type + ".jpg");

			chart.removeLegend();
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

			final JFreeChart chart = ChartFactory.createTimeSeriesChart("Tempertaure / Humidity " + title, "Date",
					"Tempertature °C", dataset1, true, true, false);

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
			chart.removeLegend();

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

	public TimeSeries getTimeSeries(String tseries, String url, String baseUrl, String type, String field)
			throws ClientProtocolException, IOException {
		return getTimeSeries(tseries, url, baseUrl, type, field, -1000);
	}

	public TimeSeries getTimeSeriesMaxMin(String tseries, String url, String baseUrl, String type, String field,
			long startMillis) throws ClientProtocolException, IOException {

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

			String tmpDate = o.get("ts").toString().replace("\"", "");

			DateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH");
			
			if(field.indexOf("Day") != -1)
			format = new SimpleDateFormat("yyyy-MM-dd");
			
			format.setTimeZone(TimeZone.getTimeZone("GMT+1"));

			Date date;
			try {
				date = format.parse(tmpDate);
			} catch (java.text.ParseException e1) {
				System.out.println("error parsing:" + tmpDate + "-->" + e1.toString());
				continue;
			}
			if (date.getTime() >= startMillis)
				m.put(date, o.get(field).getAsLong());
		}

		for (Map.Entry<Date, Long> entry : m.entrySet()) {
			serie1.add(new FixedMillisecond(entry.getKey()), entry.getValue());
			System.out.println(
					"getTimeSeriesMaxMin (" + field + ")--> key:" + entry.getKey() + " -->" + entry.getValue());
		}

		System.out.println("tot ele :" + serie1.getItems().size());
		return serie1;

	}

	public TimeSeries getTimeSeries(String tseries, String url, String baseUrl, String type, String field, int gap)
			throws ClientProtocolException, IOException {

		final TimeSeries serie1 = new TimeSeries(tseries);

		System.out.println("call " + url + baseUrl + type);

		String s = HttpHelper.getData(url + baseUrl + type);
		Gson gson = new Gson();
		final JsonArray jsonArray = gson.fromJson(s, JsonArray.class);

		final HashMap<Date, Float> m = new HashMap<Date, Float>();

		for (JsonElement e : jsonArray) {

			JsonObject o = e.getAsJsonObject();
			if (o.get(field) == null || o.get(field).isJsonNull()) {
				continue;
			}

			long limit = 1431730684000l;

			if (o.get("ts").getAsLong() < limit)
				continue;

			m.put(new Date(o.get("ts").getAsLong()), o.get(field).getAsFloat() + (gap != -1000 ? gap : 0));
		}

		for (Map.Entry<Date, Float> entry : m.entrySet()) {
			serie1.add(new FixedMillisecond(entry.getKey()), entry.getValue());
			System.out.println("getTimeSeries --> key:" + entry.getKey() + " -->" + entry.getValue());
		}

		return serie1;
	}

	public TimeSeries getTimeSeriesRain(String tseries, String url, String baseUrl, String type, String field, int gap)
			throws ClientProtocolException, IOException {

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

			Long l = o.get(field).getAsLong();

			if (l < (long) 0)
				l = new Long(0);

			m.put(new Date(o.get("ts").getAsLong()), l);
		}

		for (Map.Entry<Date, Long> entry : m.entrySet()) {
			serie1.add(new FixedMillisecond(entry.getKey()), entry.getValue());
		}
		System.out.println("tot ele :" + serie1.getItems().size());
		return serie1;
	}

}
