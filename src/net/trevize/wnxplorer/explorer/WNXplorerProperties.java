package net.trevize.wnxplorer.explorer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * WNXplorerProperties.java - Apr 6, 2010
 */

public class WNXplorerProperties {

	public static final String PROPERTIES_FILEPATH = "./WNXplorer.properties";
	public static Properties properties;

	public static final String PROPERTY_WORDNET_PATH = "PROPERTY_WORDNET_PATH";
	public static final String PROPERTY_WNXPLORER_ICON_PATH = "PROPERTY_WNXPLORER_ICON_PATH";

	private static String wordnet_path;
	private static String wnxplorer_icon_path;

	public static void loadProperties() {
		properties = new Properties();
		try {
			properties.load(new FileReader(PROPERTIES_FILEPATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		wordnet_path = properties.getProperty(PROPERTY_WORDNET_PATH);
		wnxplorer_icon_path = properties
				.getProperty(PROPERTY_WNXPLORER_ICON_PATH);
	}

	public static String getWN_PATH() {
		if (properties == null) {
			loadProperties();
		}
		return wordnet_path;
	}

	public static void setWN_PATH(String wn_path) {
		properties.setProperty(PROPERTY_WORDNET_PATH, wn_path);
		try {
			properties.store(new FileWriter(PROPERTIES_FILEPATH), "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		wordnet_path = wn_path;
	}

	public static String getWnxplorer_icon_path() {
		if (properties == null) {
			loadProperties();
		}
		return wnxplorer_icon_path;
	}

}
