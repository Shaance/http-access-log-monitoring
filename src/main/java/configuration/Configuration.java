package configuration;

public interface Configuration {

	/**
	 * @return the alert window duration in seconds on which the average section hits is calculated upon
	 */
	int getAlertWindowDuration();


	/**
	 * @return the average section hits above which an alert should be emitted
	 */
	int getAlertThreshold();

	/**
	 * @return the refresh rate in seconds of the manager in the console
	 */
	int getStatsDisplayRefreshRate();

	/**
	 * @return the http access log file path in a String
	 */
	String getLogFilePath();

}
