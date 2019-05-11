package manager;

import configuration.ConfigurationException;
import configuration.CustomConfiguration;
import configuration.DefaultConfiguration;
import configuration.ProgramOptions;
import entity.Section;
import httplog.HttpLogAccessInfo;
import httplog.HttpRequestType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectionManagerTest {

	private SectionManager sectionManager;

	@Before
	public void setUp() {
		this.sectionManager = new SectionManager(new DefaultConfiguration(), Collections.emptyList());
	}

	@Test(expected = ConfigurationException.class)
	public void exceptionWhenHavingBadConfiguration() {
		Map<ProgramOptions, String> optionMap = new HashMap<>();
		optionMap.put(ProgramOptions.ALERT_WINDOW_OPTION, "-1");
		new SectionManager(new CustomConfiguration(optionMap), null);
	}

	@Test(expected = ConfigurationException.class)
	public void exceptionWhenHavingBadConfiguration2() {
		Map<ProgramOptions, String> optionMap = new HashMap<>();
		optionMap.put(ProgramOptions.STATS_DISPLAY_RATE_OPTION, "-1");
		new SectionManager(new CustomConfiguration(optionMap), null);
	}

	@Test
	public void updateSectionHitsWithNullList() {
		try {
			Field f = SectionManager.class.getDeclaredField("sectionMap");
			f.setAccessible(true);
			sectionManager.updateSectionHits(null);
			Map<String, Section> sectionMap = (Map<String, Section>) f.get(sectionManager);
			Assert.assertTrue(sectionMap.isEmpty());
		} catch (IllegalAccessException e) {
			Assert.fail("Could not access object.");
		} catch (NoSuchFieldException e) {
			Assert.fail("Could not retrieve field.");
		}
	}

	@Test
	public void updateSectionHitsWithEmptyList() {
		try {
			Field f = SectionManager.class.getDeclaredField("sectionMap");
			f.setAccessible(true);
			sectionManager.updateSectionHits(Collections.emptyList());
			Map<String, Section> sectionMap = (Map<String, Section>) f.get(sectionManager);
			Assert.assertTrue(sectionMap.isEmpty());
		} catch (IllegalAccessException e) {
			Assert.fail("Could not access object.");
		} catch (NoSuchFieldException e) {
			Assert.fail("Could not retrieve field.");
		}
	}

	@Test
	public void updateSection() {
		try {
			Field f = SectionManager.class.getDeclaredField("sectionMap");
			f.setAccessible(true);
			String sectionName = "mySection";
			List<HttpLogAccessInfo> infoList = Collections.singletonList(
					new HttpLogAccessInfo("test", null, null,
							HttpRequestType.GET, sectionName, 200)
			);
			sectionManager.updateSectionHits(infoList);
			Map<String, Section> sectionMap = (Map<String, Section>) f.get(sectionManager);
			Assert.assertFalse(sectionMap.isEmpty());
			Assert.assertTrue(sectionMap.containsKey(sectionName));
		} catch (IllegalAccessException e) {
			Assert.fail("Could not access object.");
		} catch (NoSuchFieldException e) {
			Assert.fail("Could not retrieve field.");
		}
	}
}