package configuration;

public class DefaultConfiguration implements Configuration {

	private int alertWindowDuration;
	private int alertThreshold;
	private int statsDisplayRefreshRate;
	private String logFilePath;

	public DefaultConfiguration() {
		this.alertWindowDuration = 120;
		this.alertThreshold = 10;
		this.statsDisplayRefreshRate = 10;
		this.logFilePath = "/tmp/access.log";
	}

	@Override
	public int getAlertWindowDuration() {
		return alertWindowDuration;
	}

	void setAlertWindowDuration(int alertWindowDuration) {
		this.alertWindowDuration = alertWindowDuration;
	}

	@Override
	public int getAlertThreshold() {
		return alertThreshold;
	}

	void setAlertThreshold(int alertThreshold) {
		this.alertThreshold = alertThreshold;
	}

	@Override
	public int getStatsDisplayRefreshRate() {
		return statsDisplayRefreshRate;
	}

	void setStatsDisplayRefreshRate(int statsDisplayRefreshRate) {
		this.statsDisplayRefreshRate = statsDisplayRefreshRate;
	}

	@Override
	public String getLogFilePath() {
		return logFilePath;
	}

	void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
}
