package net.trevize.knetvis;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * KNetVisProperties.java - Apr 6, 2010
 */

public class KNetVisProperties {

	public static final String PROPERTIES_FILEPATH = "./KNetVis.properties";
	public static Properties properties;

	public static final String PROPERTY_ICON_PATH_GO_PREVIOUS = "PROPERTY_ICON_PATH_GO_PREVIOUS";
	public static final String PROPERTY_ICON_PATH_GO_NEXT = "PROPERTY_ICON_PATH_GO_NEXT";
	public static final String PROPERTY_ICON_PATH_HELP = "PROPERTY_ICON_PATH_HELP";
	public static final String PROPERTY_ICON_PATH_SEMANTIC_RELATION_SELECTOR = "PROPERTY_ICON_PATH_SEMANTIC_RELATION_SELECTOR";
	public static final String PROPERTY_ICON_PATH_CLOSE = "PROPERTY_ICON_PATH_CLOSE";
	public static final String PROPERTY_STYLESHEET_PATH_CONCEPT_DESCRIPTION_PANEL = "PROPERTY_STYLESHEET_PATH_CONCEPT_DESCRIPTION_PANEL";

	private static String icon_path_go_previous;
	private static String icon_path_go_next;
	private static String icon_path_help;
	private static String icon_path_semantic_relation_selector;
	private static String icon_path_close;
	private static String stylesheet_path_concept_description_panel;

	public static void loadProperties() {
		properties = new Properties();
		try {
			properties.load(new FileReader(PROPERTIES_FILEPATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		icon_path_go_previous = properties
				.getProperty(PROPERTY_ICON_PATH_GO_PREVIOUS);
		icon_path_go_next = properties.getProperty(PROPERTY_ICON_PATH_GO_NEXT);
		icon_path_help = properties.getProperty(PROPERTY_ICON_PATH_HELP);
		icon_path_semantic_relation_selector = properties
				.getProperty(PROPERTY_ICON_PATH_SEMANTIC_RELATION_SELECTOR);
		icon_path_close = properties.getProperty(PROPERTY_ICON_PATH_CLOSE);
		stylesheet_path_concept_description_panel = properties
				.getProperty(PROPERTY_STYLESHEET_PATH_CONCEPT_DESCRIPTION_PANEL);
	}

	public static String getIcon_path_go_previous() {
		if (properties == null) {
			loadProperties();
		}
		return icon_path_go_previous;
	}

	public static String getIcon_path_go_next() {
		if (properties == null) {
			loadProperties();
		}
		return icon_path_go_next;
	}

	public static String getIcon_path_help() {
		if (properties == null) {
			loadProperties();
		}
		return icon_path_help;
	}

	public static String getIcon_path_semantic_relation_selector() {
		if (properties == null) {
			loadProperties();
		}
		return icon_path_semantic_relation_selector;
	}

	public static String getIcon_path_close() {
		if (properties == null) {
			loadProperties();
		}
		return icon_path_close;
	}

	public static String getStylesheet_path_concept_description_panel() {
		if (properties == null) {
			loadProperties();
		}
		return stylesheet_path_concept_description_panel;
	}

}
