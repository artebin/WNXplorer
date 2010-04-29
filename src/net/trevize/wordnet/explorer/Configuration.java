package net.trevize.wordnet.explorer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * Configuration.java - Apr 6, 2010
 */

public class Configuration {

	public static final String CONFIG_PROPERTIES_PATH = "./config.properties";
	public static Properties config_properties;

	public static final String PROP_WN_PATH = "WN_PATH";
	public static final String PROP_LUCENEWN_PATH = "LUCENEWN_PATH";

	public static String WN_PATH;
	public static String LUCENEWN_PATH;

	public static void init() {
		config_properties = new Properties();
		try {
			config_properties.load(new FileReader(CONFIG_PROPERTIES_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		WN_PATH = config_properties.getProperty(PROP_WN_PATH);
		LUCENEWN_PATH = config_properties.getProperty(PROP_LUCENEWN_PATH);
	}

	public static String getLUCENEWN_PATH() {
		if (config_properties == null) {
			init();
		}
		return LUCENEWN_PATH;
	}

	public static String getWN_PATH() {
		if (config_properties == null) {
			init();
		}
		return WN_PATH;
	}

}
