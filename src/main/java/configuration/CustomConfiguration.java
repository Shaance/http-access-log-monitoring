package configuration;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomConfiguration extends DefaultConfiguration {

	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	public CustomConfiguration(Map<ProgramOptions, String> optionMap) {
		overrideConfiguration(optionMap);
	}

	private void overrideConfiguration(Map<ProgramOptions, String> optionMap) {

		try {
			for (Map.Entry<ProgramOptions, String> entry : optionMap.entrySet()) {

				if (ProgramOptions.STATS_DISPLAY_RATE_OPTION.equals(entry.getKey())) {
					this.setStatsDisplayRefreshRate(Integer.parseInt(entry.getValue()));
				} else if (ProgramOptions.ALERT_THRESHOLD_OPTION.equals(entry.getKey())) {
					this.setAlertThreshold(Integer.parseInt(entry.getValue()));
				} else if (ProgramOptions.ALERT_WINDOW_OPTION.equals(entry.getKey())) {
					this.setAlertWindowDuration(Integer.parseInt(entry.getValue()));
				} else if (ProgramOptions.PATH_OPTION.equals(entry.getKey())) {
					this.setLogFilePath(entry.getValue());
				}

			}
		} catch (NumberFormatException nfe) {
			LOGGER.log(Level.SEVERE, nfe.getMessage());
			throw new ConfigurationException("Incorrect value format value.", nfe);
		} catch (NullPointerException npe) {
			LOGGER.log(Level.SEVERE, npe.getMessage());
			throw new ConfigurationException("The input cannot be null.", npe);
		}

	}


}
