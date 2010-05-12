package net.trevize.wordnet.explorer;

import java.io.FileNotFoundException;
import java.io.FileReader;
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

	private static String wordnet_path;

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
	}

	public static String getWN_PATH() {
		if (properties == null) {
			loadProperties();
		}
		return wordnet_path;
	}

}
