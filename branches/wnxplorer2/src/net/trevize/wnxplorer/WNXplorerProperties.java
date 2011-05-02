package net.trevize.wnxplorer;

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
	public static final String PROPERTY_NUM_OF_RESULTS_PER_PAGE_INTO_SEARCH_PANEL = "PROPERTY_NUM_OF_RESULTS_PER_PAGE_INTO_SEARCH_PANEL";
	public static final String PROPERTY_ICON_PATH_GO_PREVIOUS = "PROPERTY_ICON_PATH_GO_PREVIOUS";
	public static final String PROPERTY_ICON_PATH_GO_NEXT = "PROPERTY_ICON_PATH_GO_NEXT";
	public static final String PROPERTY_ICON_PATH_HELP = "PROPERTY_ICON_PATH_HELP";
	public static final String PROPERTY_ICON_PATH_POINTER_SELECTOR = "PROPERTY_ICON_PATH_POINTER_SELECTOR";
	public static final String PROPERTY_ICON_CLOSE = "PROPERTY_ICON_CLOSE";
	public static final String PROPERTY_RESULTS_PANEL_STYLESHEET_FILEPATH = "PROPERTY_RESULTS_PANEL_STYLESHEET_FILEPATH";
	public static final String PROPERTY_SYNSET_INFO_PANEL_STYLESHEET_FILEPATH = "PROPERTY_SYNSET_INFO_PANEL_STYLESHEET_FILEPATH";
	public static final String PROPERTY_HELP_DIALOG_STYLESHEET_FILEPATH = "PROPERTY_HELP_DIALOG_STYLESHEET_FILEPATH";
	public static final String PROPERTY_HELP_HTML_FILEPATH = "PROPERTY_HELP_HTML_FILEPATH";

	private static String wordnet_path;
	private static String wnxplorer_icon_path;
	private static int num_of_results_per_page_into_search_panel;
	private static String icon_path_go_previous;
	private static String icon_path_go_next;
	private static String icon_path_help;
	private static String icon_path_pointer_selector;
	private static String icon_path_close;
	private static String results_panel_stylesheet_filepath;
	private static String synset_info_panel_stylesheet_filepath;
	private static String help_dialog_stylesheet_filepath;
	private static String help_html_filepath;

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
		num_of_results_per_page_into_search_panel = Integer
				.parseInt(properties
						.getProperty(PROPERTY_NUM_OF_RESULTS_PER_PAGE_INTO_SEARCH_PANEL));
		icon_path_go_previous = properties
				.getProperty(PROPERTY_ICON_PATH_GO_PREVIOUS);
		icon_path_go_next = properties.getProperty(PROPERTY_ICON_PATH_GO_NEXT);
		icon_path_help = properties.getProperty(PROPERTY_ICON_PATH_HELP);
		icon_path_pointer_selector = properties
				.getProperty(PROPERTY_ICON_PATH_POINTER_SELECTOR);
		results_panel_stylesheet_filepath = properties
				.getProperty(PROPERTY_RESULTS_PANEL_STYLESHEET_FILEPATH);
		synset_info_panel_stylesheet_filepath = properties
				.getProperty(PROPERTY_SYNSET_INFO_PANEL_STYLESHEET_FILEPATH);
		help_dialog_stylesheet_filepath = properties
				.getProperty(PROPERTY_HELP_DIALOG_STYLESHEET_FILEPATH);
		help_html_filepath = properties
				.getProperty(PROPERTY_HELP_HTML_FILEPATH);
		icon_path_close = properties.getProperty(PROPERTY_ICON_CLOSE);
	}

	public static String getWN_PATH() {
		if (properties == null) {
			loadProperties();
		}
		return wordnet_path;
	}

	public static void setWN_PATH(String wn_path) {
		if (properties == null) {
			loadProperties();
		}
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

	public static int getNum_of_results_per_page_into_search_panel() {
		if (properties == null) {
			loadProperties();
		}
		return num_of_results_per_page_into_search_panel;
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

	public static String getResults_panel_stylesheet_filepath() {
		if (properties == null) {
			loadProperties();
		}
		return results_panel_stylesheet_filepath;
	}

	public static String getSynset_info_panel_stylesheet_filepath() {
		if (properties == null) {
			loadProperties();
		}
		return synset_info_panel_stylesheet_filepath;
	}

	public static String getIcon_path_help() {
		if (properties == null) {
			loadProperties();
		}
		return icon_path_help;
	}

	public static String getIcon_path_pointer_selector() {
		if (properties == null) {
			loadProperties();
		}
		return icon_path_pointer_selector;
	}

	public static String getHelp_dialog_stylesheet_filepath() {
		if (properties == null) {
			loadProperties();
		}
		return help_dialog_stylesheet_filepath;
	}

	public static String getHelp_html_filepath() {
		if (properties == null) {
			loadProperties();
		}
		return help_html_filepath;
	}

	public static String getIcon_path_close() {
		if (properties == null) {
			loadProperties();
		}
		return icon_path_close;
	}

}
