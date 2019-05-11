package configuration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;

public class ConfigurationManagerTest {

	private ConfigurationManager configurationManager;
	private ConfigurationParser configurationParser;

	@Before
	public void setUp() {
		configurationParser = Mockito.mock(ConfigurationParserImpl.class);
		configurationManager = new ConfigurationManager(configurationParser);
	}

	@Test
	public void getConfigurationWithNoArgs() {
		Assert.assertTrue(configurationManager.getConfiguration(new String[0]) instanceof DefaultConfiguration);
	}

	@Test
	public void getConfigurationWithNullArgs() {
		Assert.assertTrue(configurationManager.getConfiguration(null) instanceof DefaultConfiguration);
	}

	@Test
	public void getConfigurationWithArgs() {
		String[] args = new String[2];
		Mockito.when(configurationParser.getProgramOptionMap(args)).thenReturn(new HashMap<>());
		Assert.assertTrue(configurationManager.getConfiguration(args) instanceof CustomConfiguration);
	}

}