package it.marcoberri.mbmeteo.chart;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 
 * Sample -url http://meteo.marcoberri.it/T/24 -f /tmp/test.jpg
 * 
 * @author berri
 *
 */
public class Main {

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

				try {
					String s = HttpHelper.getData(cmd.getOptionValue("url"));
					System.out.println(s);

					final TimeSeriesCollection dataset1 = new TimeSeriesCollection();
					dataset1.setDomainIsPointsInTime(true);

					final TimeSeries serie1 = new TimeSeries("T°", Minute.class);

					Gson gson = new Gson();
					JsonArray jsonArray = gson.fromJson(s, JsonArray.class);

					for (JsonElement e : jsonArray) {
						JsonObject o = e.getAsJsonObject();
						serie1.add(new Minute(new Date(o.get("ts").getAsInt())), o.get("T1").getAsInt());
					}

					dataset1.addSeries(serie1);
					
					String s1 = HttpHelper.getData("http://meteo.marcoberri.it/H/24");
					
					final TimeSeries serie2 = new TimeSeries("H%", Minute.class);

					final TimeSeriesCollection dataset2 = new TimeSeriesCollection();

					JsonArray jsonArray2 = gson.fromJson(s1, JsonArray.class);

					for (JsonElement e : jsonArray2) {
						JsonObject o = e.getAsJsonObject();
						serie2.add(new Minute(new Date(o.get("ts").getAsInt())), o.get("H1").getAsInt());
					}

					dataset2.addSeries(serie2);

					
					

					final JFreeChart chart = ChartFactory.createTimeSeriesChart("T°/H% last 24", "Date", "T°", dataset1, true, true, false);

					final XYPlot plot = chart.getXYPlot();
					final NumberAxis axis2 = new NumberAxis("Humidity %");
					axis2.setAutoRangeIncludesZero(false);
					plot.setRangeAxis(1, axis2);
					plot.setDataset(1, dataset2);
					plot.mapDatasetToRangeAxis(1, 1);
					final XYItemRenderer renderer = plot.getRenderer();
					renderer.setToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
					if (renderer instanceof StandardXYItemRenderer) {
						final StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
						rr.setShapesFilled(true);
					}

				
					final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
					renderer2.setSeriesPaint(0, Color.blue);
					renderer.setToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
					plot.setRenderer(1, renderer2);


					
					final DateAxis axis = (DateAxis) plot.getDomainAxis();
					axis.setDateFormatOverride(new SimpleDateFormat("hh:mma"));

					
					
					
					

					int width = 800;
					int height = 300;
					File lineChart = new File(cmd.getOptionValue("f"));
					ChartUtilities.saveChartAsJPEG(lineChart, chart, width, height);

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -jar <jar_name>" + "\nVersion:" + ConfigurationHelper.getProperties().getProperty("app.version") + "\nbuild: " + ConfigurationHelper.getProperties().getProperty("app.build"), options);
			}

		} catch (final ParseException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}
