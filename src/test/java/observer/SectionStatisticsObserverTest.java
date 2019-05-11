package observer;

import configuration.ConfigurationException;
import console.ConsolePrinter;
import console.ConsolePrinterImpl;
import entity.Section;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import entity.SectionHits;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Observer;

public class SectionStatisticsObserverTest {

	private static final int NUMBER_OF_ELEMENTS = 3;
	private static final int STATS_INTERVAL = 10;

	private ConsolePrinter consolePrinterMock;

	private SectionStatisticsObserver sectionStatisticsObserver;

	@Before
	public void setUp() {
		consolePrinterMock = Mockito.mock(ConsolePrinterImpl.class);
		sectionStatisticsObserver = new SectionStatisticsObserver(NUMBER_OF_ELEMENTS, consolePrinterMock);
	}

	@Test(expected = ConfigurationException.class)
	public void negativeNumberOfElements(){
		sectionStatisticsObserver = new SectionStatisticsObserver(-1, consolePrinterMock);
	}

	@Test
	public void update() {
		try {
			Field f = SectionStatisticsObserver.class.getDeclaredField("sectionMap");
			f.setAccessible(true);
			Section section = getCustomSectionHit("test", 10);
			sectionStatisticsObserver.update(section, null);
			Map<String, Section> sectionMap = (Map<String, Section>) f.get(sectionStatisticsObserver);
			Assert.assertFalse(sectionMap.isEmpty());
			Assert.assertEquals(section, sectionMap.get("test"));
		} catch (IllegalAccessException e) {
			Assert.fail("Could not access object.");
		} catch (NoSuchFieldException e) {
			Assert.fail("Could not retrieve field.");
		}
	}

	@Test
	public void nothingIsPrintedWhenSectionMapIsEmpty() {
		sectionStatisticsObserver.run();
		Mockito.verify(consolePrinterMock, Mockito.never()).printStatsBanner();
		Mockito.verify(consolePrinterMock, Mockito.never()).printIntervalTopSections(Mockito.anyCollection());
		Mockito.verify(consolePrinterMock, Mockito.never()).printAllTimeTopSections(Mockito.anyCollection());
	}

	@Test
	public void intervalTopSectionsDisplayedCorrectly() {
		Section first = getCustomSectionHit("1st", 50);
		Section second = getCustomSectionHit("2nd", 30);
		Section third = getCustomSectionHit("3rd", 10);

		sectionStatisticsObserver.update(second, null);
		sectionStatisticsObserver.update(third, null);
		sectionStatisticsObserver.update(first, null);

		sectionStatisticsObserver.run();
		Mockito.verify(consolePrinterMock, Mockito.times(1)).printStatsBanner();

		ArgumentCaptor<List<SectionHits>> intervalTopInterceptor = ArgumentCaptor.forClass(List.class);
		Mockito.verify(consolePrinterMock, Mockito.times(1)).printIntervalTopSections(intervalTopInterceptor.capture());
		Mockito.verify(consolePrinterMock, Mockito.times(1)).printAllTimeTopSections(Mockito.anyCollection());

		final List<SectionHits> intervalTops = intervalTopInterceptor.getValue();
		List<SectionHits> expectedTop = Arrays.asList(new SectionHits("1st", 50 * STATS_INTERVAL),
				new SectionHits("2nd", 30 * STATS_INTERVAL),
				new SectionHits("3rd", 10 * STATS_INTERVAL));

		List<SectionHits> wrongTopList = Arrays.asList(new SectionHits("3rd", 10 * STATS_INTERVAL),
				new SectionHits("2nd", 30 * STATS_INTERVAL),
				new SectionHits("1st", 50 * STATS_INTERVAL));

		Assert.assertEquals(expectedTop, intervalTops);
		Assert.assertNotEquals(wrongTopList, intervalTops);

	}

	@Test
	public void allTimeAndIntervalTopSectionsDisplayedCorrectly() {
		Section first = getCustomSectionHit("1st", 15);
		Section second = getCustomSectionHit("2nd", 20);
		Section third = getCustomSectionHit("3rd", 16);

		//the section named 1st should be 3rd for interval top sections for 1st in all time
		for (int i = 0; i < STATS_INTERVAL; i++) {
			first.updateHits(6);
		}

		sectionStatisticsObserver.update(second, null);
		sectionStatisticsObserver.update(third, null);
		sectionStatisticsObserver.update(first, null);

		sectionStatisticsObserver.run();
		Mockito.verify(consolePrinterMock, Mockito.times(1)).printStatsBanner();

		ArgumentCaptor<List<SectionHits>> intervalTopInterceptor = ArgumentCaptor.forClass(List.class);
		ArgumentCaptor<List<SectionHits>> allTimeTopInterceptor = ArgumentCaptor.forClass(List.class);
		Mockito.verify(consolePrinterMock, Mockito.times(1)).printIntervalTopSections(intervalTopInterceptor.capture());
		Mockito.verify(consolePrinterMock, Mockito.times(1)).printAllTimeTopSections(allTimeTopInterceptor.capture());

		final List<SectionHits> intervalTops = intervalTopInterceptor.getValue();

		List<SectionHits> expectedTop = Arrays.asList(new SectionHits("2nd", 20 * STATS_INTERVAL),
				new SectionHits("3rd", 16 * STATS_INTERVAL),
				new SectionHits("1st", 6 * STATS_INTERVAL));

		List<SectionHits> wrongTopList = Arrays.asList(new SectionHits("1st", 6 * STATS_INTERVAL),
				new SectionHits("2nd", 20 * STATS_INTERVAL),
				new SectionHits("3rd", 16 * STATS_INTERVAL));

		Assert.assertEquals(expectedTop, intervalTops);
		Assert.assertNotEquals(wrongTopList, intervalTops);

		final List<SectionHits> allTimeTops = allTimeTopInterceptor.getValue();

		expectedTop = Arrays.asList(new SectionHits("1st", 21 * STATS_INTERVAL),
				new SectionHits("2nd", 20 * STATS_INTERVAL),
				new SectionHits("3rd", 16 * STATS_INTERVAL));

		wrongTopList = Arrays.asList(new SectionHits("2nd", 20 * STATS_INTERVAL),
				new SectionHits("3rd", 16 * STATS_INTERVAL),
				new SectionHits("1st", 6 * STATS_INTERVAL));

		Assert.assertEquals(expectedTop, allTimeTops);
		Assert.assertNotEquals(wrongTopList, allTimeTops);

	}

	private Section getCustomSectionHit(String sectionName, int averageHitDuringInterval) {
		List<Observer> observers = Arrays.asList(new SectionAlertObserver(1, 1, Mockito.mock(ConsolePrinter.class)),
				new SectionStatisticsObserver(2, Mockito.mock(ConsolePrinter.class)));
		Section section = new Section(sectionName, 120, STATS_INTERVAL, observers);

		for (int i = 0; i < STATS_INTERVAL; i++) {
			section.updateHits(averageHitDuringInterval);
		}

		return section;
	}

}