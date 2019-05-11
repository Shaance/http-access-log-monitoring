package configuration;

import java.util.Map;

interface ConfigurationParser {

	/**
	 * This method aims to extract the ProgramOptions and the value associated from the program parameters
	 *
	 * @param args the String array from which the options will be extracted
	 * @return a map with the program options and its associated value, the values are not checked, meaning that
	 * an error can occur later on in the program
	 */
	Map<ProgramOptions, String> getProgramOptionMap(String[] args);

}
