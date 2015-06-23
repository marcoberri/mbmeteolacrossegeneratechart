package it.marcoberri.mbmeteo.chart;

import java.io.IOException;
import java.util.Properties;

public class ConfigurationHelper {

	private static final Properties properties = initProperties();

	private ConfigurationHelper() {
	}

	private static Properties initProperties() {
		final Properties prop = new Properties();

		Properties p = new Properties();
		try {
			p.load(ConfigurationHelper.class.getResourceAsStream("/version.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		prop.putAll(p);
		return prop;
	}

	public static String getProperty(IProperty prop) {
		return getProperty(prop.getKey());
	}

	public static String getProperty(String name) {
		return properties.getProperty(name);
	}

	public static String getProperty(IProperty prop, String defaultValue) {
		return getProperty(prop.getKey(), defaultValue);
	}

	public static String getProperty(String name, String defaultValue) {
		return properties.getProperty(name, defaultValue);
	}

	public static Properties getProperties() {
		return properties;
	}

}
