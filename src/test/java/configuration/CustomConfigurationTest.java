package configuration;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CustomConfigurationTest {

	@Test(expected = ConfigurationException.class)
	public void nullMap() {
		new CustomConfiguration(null);
	}

	@Test(expected = ConfigurationException.class)
	public void numberFormatException() {
		Map<ProgramOptions, String> optionMap = new HashMap<>();
		optionMap.put(ProgramOptions.ALERT_WINDOW_OPTION, "dddd");
		new CustomConfiguration(optionMap);
	}

	@Test
	public void overrideStatsDisplay() {
		Map<ProgramOptions, String> optionMap = new HashMap<>();
		String optionValue = "999";
		optionMap.put(ProgramOptions.STATS_DISPLAY_RATE_OPTION, optionValue);
		CustomConfiguration customConfiguration = new CustomConfiguration(optionMap);
		Assert.assertEquals(Integer.parseInt(optionValue), customConfiguration.getStatsDisplayRefreshRate());
		//default values
		Assert.assertEquals(10, customConfiguration.getAlertThreshold());
		Assert.assertEquals(120, customConfiguration.getAlertWindowDuration());
		Assert.assertEquals("/tmp/access.log", customConfiguration.getLogFilePath());
	}

	@Test
	public void overrideAlertThreshold() {
		Map<ProgramOptions, String> optionMap = new HashMap<>();
		String optionValue = "999";
		optionMap.put(ProgramOptions.ALERT_THRESHOLD_OPTION, optionValue);
		CustomConfiguration customConfiguration = new CustomConfiguration(optionMap);
		Assert.assertEquals(Integer.parseInt(optionValue), customConfiguration.getAlertThreshold());
		//default values
		Assert.assertEquals(120, customConfiguration.getAlertWindowDuration());
		Assert.assertEquals(10, customConfiguration.getStatsDisplayRefreshRate());
		Assert.assertEquals("/tmp/access.log", customConfiguration.getLogFilePath());
	}

	@Test
	public void overrideAlertWindowDuration() {
		Map<ProgramOptions, String> optionMap = new HashMap<>();
		String optionValue = "999";
		optionMap.put(ProgramOptions.ALERT_WINDOW_OPTION, optionValue);
		CustomConfiguration customConfiguration = new CustomConfiguration(optionMap);
		Assert.assertEquals(Integer.parseInt(optionValue), customConfiguration.getAlertWindowDuration());
		//default values
		Assert.assertEquals(10, customConfiguration.getAlertThreshold());
		Assert.assertEquals(10, customConfiguration.getStatsDisplayRefreshRate());
		Assert.assertEquals("/tmp/access.log", customConfiguration.getLogFilePath());
	}

	@Test
	public void overrideLogPath() {
		Map<ProgramOptions, String> optionMap = new HashMap<>();
		String optionValue = "/custom/path";
		optionMap.put(ProgramOptions.PATH_OPTION, optionValue);
		CustomConfiguration customConfiguration = new CustomConfiguration(optionMap);
		Assert.assertEquals(optionValue, customConfiguration.getLogFilePath());
		//default values
		Assert.assertEquals(10, customConfiguration.getAlertThreshold());
		Assert.assertEquals(10, customConfiguration.getStatsDisplayRefreshRate());
		Assert.assertEquals(120, customConfiguration.getAlertWindowDuration());
	}


}