package configuration;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationParserImpl implements ConfigurationParser {

	public Map<ProgramOptions, String> getProgramOptionMap(String[] args) {
		Map<ProgramOptions, String> result = new HashMap<>();

		if (args != null && args.length % 2 == 0) {
			for (int i = 1; i < args.length; i += 2) {
				String parsedOption = args[i - 1];
				String parsedValue = args[i];

				if (parsedOption.length() > 1) {
					ProgramOptions option = ProgramOptions.valueOfLabel(parsedOption.substring(1));
					if (null != option && parsedOption.charAt(0) == '-') {
						result.put(option, parsedValue);
					} else {
						throw new ConfigurationException("Unrecognized option: " + parsedOption);
					}
				} else {
					throw new ConfigurationException("Incorrect length for option " + parsedOption);
				}
			}
		} else {
			throw new ConfigurationException("Incorrect length for configuration, missing option or value.");
		}

		return result;
	}

}
