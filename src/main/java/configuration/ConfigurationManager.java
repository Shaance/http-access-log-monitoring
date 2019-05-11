package configuration;

public class ConfigurationManager {

	private ConfigurationParser configurationParser;

	public ConfigurationManager(ConfigurationParser configurationParser) {
		this.configurationParser = configurationParser;
	}

	public Configuration getConfiguration(String[] args) {
		if (args == null || args.length == 0) {
			return new DefaultConfiguration();
		} else {
			return new CustomConfiguration(configurationParser.getProgramOptionMap(args));
		}
	}

}
