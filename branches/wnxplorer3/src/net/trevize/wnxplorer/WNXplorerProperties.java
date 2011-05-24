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
	private static final String PROPERTIES_COMMENTS = "This is the properties file of WNXplorer, a Java application for navigating into WordNet\n#Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]";

	public static final String PROPERTY_WORDNET_DICT_PATH = "PROPERTY_WORDNET_DICT_PATH";
	public static final String PROPERTY_NUM_OF_RESULTS_PER_PAGE_INTO_SEARCH_PANEL = "PROPERTY_NUM_OF_RESULTS_PER_PAGE_INTO_SEARCH_PANEL";
	public static final String PROPERTY_ICON_PATH_WNXPLORER = "PROPERTY_ICON_PATH_WNXPLORER";
	public static final String PROPERTY_ICON_PATH_GO_PREVIOUS = "PROPERTY_ICON_PATH_GO_PREVIOUS";
	public static final String PROPERTY_ICON_PATH_GO_NEXT = "PROPERTY_ICON_PATH_GO_NEXT";
	public static final String PROPERTY_ICON_PATH_HELP = "PROPERTY_ICON_PATH_HELP";
	public static final String PROPERTY_ICON_PATH_SEMANTIC_RELATION_SELECTOR = "PROPERTY_ICON_PATH_SEMANTIC_RELATION_SELECTOR";
	public static final String PROPERTY_ICON_PATH_CLOSE = "PROPERTY_ICON_PATH_CLOSE";
	public static final String PROPERTY_CSS_PATH_MAIN = "PROPERTY_CSS_PATH_MAIN";
	public static final String PROPERTY_HTML_PATH_ABOUT = "PROPERTY_HTML_PATH_ABOUT";
	public static final String PROPERTY_HTML_PATH_HELP = "PROPERTY_HTML_PATH_HELP";
	public static final String PROPERTY_DOCKING_WINDOWS_THEME = "PROPERTY_DOCKING_WINDOWS_THEME";
	public static final String PROPERTY_LUCENE_WORDNET_INDEX_PATH = "PROPERTY_LUCENE_WORDNET_INDEX_PATH";

	private static String wordnet_dict_path;
	private static int num_of_results_per_page_into_search_panel;
	private static String icon_path_wnxplorer;
	private static String icon_path_go_previous;
	private static String icon_path_go_next;
	private static String icon_path_help;
	private static String icon_path_semantic_relation_selector;
	private static String icon_path_close;
	private static String css_path_main;
	private static String html_path_about;
	private static String html_path_help;
	private static String docking_windows_theme;
	private static String lucene_wordnet_index_path;

	public static void loadProperties() {
		properties = new Properties();
		try {
			properties.load(new FileReader(PROPERTIES_FILEPATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		wordnet_dict_path = properties.getProperty(PROPERTY_WORDNET_DICT_PATH);
		icon_path_wnxplorer = properties
				.getProperty(PROPERTY_ICON_PATH_WNXPLORER);
		num_of_results_per_page_into_search_panel = Integer
				.parseInt(properties
						.getProperty(PROPERTY_NUM_OF_RESULTS_PER_PAGE_INTO_SEARCH_PANEL));
		icon_path_go_previous = properties
				.getProperty(PROPERTY_ICON_PATH_GO_PREVIOUS);
		icon_path_go_next = properties.getProperty(PROPERTY_ICON_PATH_GO_NEXT);
		icon_path_help = properties.getProperty(PROPERTY_ICON_PATH_HELP);
		icon_path_semantic_relation_selector = properties
				.getProperty(PROPERTY_ICON_PATH_SEMANTIC_RELATION_SELECTOR);
		icon_path_close = properties.getProperty(PROPERTY_ICON_PATH_CLOSE);
		css_path_main = properties.getProperty(PROPERTY_CSS_PATH_MAIN);
		html_path_about = properties.getProperty(PROPERTY_HTML_PATH_ABOUT);
		html_path_help = properties.getProperty(PROPERTY_HTML_PATH_HELP);
		docking_windows_theme = properties
				.getProperty(PROPERTY_DOCKING_WINDOWS_THEME);
		lucene_wordnet_index_path = properties
				.getProperty(PROPERTY_LUCENE_WORDNET_INDEX_PATH);
	}

	public static String getWordnet_dict_path() {
		if (properties == null) {
			loadProperties();
		}
		return wordnet_dict_path;
	}

	public static void setWordnet_dict_path(String wordnet_dict_path) {
		if (properties == null) {
			loadProperties();
		}
		properties.setProperty(PROPERTY_WORDNET_DICT_PATH, wordnet_dict_path);
		try {
			properties.store(new FileWriter(PROPERTIES_FILEPATH),
					PROPERTIES_COMMENTS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		WNXplorerProperties.wordnet_dict_path = wordnet_dict_path;
	}

	public static int getNum_of_results_per_page_into_search_panel() {
		if (properties == null) {
			loadProperties();
		}
		return num_of_results_per_page_into_search_panel;
	}

	public static String getIcon_path_wnxplorer() {
		if (properties == null) {
			loadProperties();
		}
		return icon_path_wnxplorer;
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

	public static String getCss_path_main() {
		if (properties == null) {
			loadProperties();
		}
		return css_path_main;
	}

	public static String getHtml_path_about() {
		if (properties == null) {
			loadProperties();
		}
		return html_path_about;
	}

	public static String getHtml_path_help() {
		if (properties == null) {
			loadProperties();
		}
		return html_path_help;
	}

	public static String getDocking_windows_theme() {
		if (properties == null) {
			loadProperties();
		}
		return docking_windows_theme;
	}

	public static String getLucene_wordnet_index_path() {
		if (properties == null) {
			loadProperties();
		}
		return lucene_wordnet_index_path;
	}

}
