package configuration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class ConfigurationParserImplTest {

	private ConfigurationParser configurationParser;

	@Before
	public void setUp() {
		configurationParser = new ConfigurationParserImpl();
	}

	@Test(expected = ConfigurationException.class)
	public void nullArgs() {
		configurationParser.getProgramOptionMap(null);
	}

	@Test(expected = ConfigurationException.class)
	public void invalidNumberOfArguments() {
		configurationParser.getProgramOptionMap(new String[1]);
	}

	@Test(expected = ConfigurationException.class)
	public void invalidLengthOfOption() {
		String[] args = new String[2];
		args[0] = "D";
		configurationParser.getProgramOptionMap(args);
	}

	@Test(expected = ConfigurationException.class)
	public void unknownOption() {
		String[] args = new String[2];
		args[0] = "-D";
		configurationParser.getProgramOptionMap(args);
	}

	@Test(expected = ConfigurationException.class)
	public void badOptionFormat() {
		String[] args = new String[2];
		args[0] = "+P";
		configurationParser.getProgramOptionMap(args);
	}

	@Test
	public void getProgramOptionMap() {
		String[] args = new String[2];
		args[0] = "-P";
		Map<ProgramOptions, String> result = configurationParser.getProgramOptionMap(args);
		Assert.assertNotNull(result);
		Assert.assertFalse(result.isEmpty());
		assertEquals(1, result.size());
	}

}