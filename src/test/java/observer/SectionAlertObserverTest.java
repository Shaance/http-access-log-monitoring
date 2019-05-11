package observer;

import console.ConsolePrinter;
import console.ConsolePrinterImpl;
import entity.Section;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Observer;

public class SectionAlertObserverTest {

	private static final int ALERT_INTERVAL = 10;
	private static final int ALERT_THRESHOLD = 5;

	private SectionAlertObserver sectionAlertObserver;

	private ConsolePrinter consolePrinterMock;

	@Before
	public void setUp() {
		consolePrinterMock = Mockito.mock(ConsolePrinterImpl.class);
		sectionAlertObserver = new SectionAlertObserver(ALERT_INTERVAL, ALERT_THRESHOLD, consolePrinterMock);
	}

	@Test
	public void highTrafficSectionWillRaiseAlert() {
		Section section = getSectionWithAverageHit("test", 8);
		sectionAlertObserver.update(section, null);
		Mockito.verify(consolePrinterMock, Mockito.times(1))
				.printRaiseAlert(Mockito.anyString(), Mockito.anyLong(), Mockito.any(LocalDateTime.class));

	}

	@Test
	public void alreadyInAlertSectionWillNotRaiseAnotherAlert() {
		Section section = getSectionWithAverageHit("test", 8);
		sectionAlertObserver.update(section, null);
		Section section2 = getSectionWithAverageHit("test", 9);
		sectionAlertObserver.update(section2, null);
		Mockito.verify(consolePrinterMock, Mockito.times(1))
				.printRaiseAlert(Mockito.anyString(), Mockito.anyLong(), Mockito.any(LocalDateTime.class));

	}

	@Test
	public void multipleAlertRaisedForDifferentSections() {
		Section section = getSectionWithAverageHit("test", 8);
		sectionAlertObserver.update(section, null);
		Section section2 = getSectionWithAverageHit("test2", 9);
		sectionAlertObserver.update(section2, null);
		Section section3 = getSectionWithAverageHit("test3", 9);
		sectionAlertObserver.update(section3, null);
		Mockito.verify(consolePrinterMock, Mockito.times(3))
				.printRaiseAlert(Mockito.anyString(), Mockito.anyLong(), Mockito.any(LocalDateTime.class));
	}

	@Test
	public void alertRecover() {
		Section section = getSectionWithAverageHit("test", 8);
		sectionAlertObserver.update(section, null);
		Section section2 = getSectionWithAverageHit("test", 3);
		sectionAlertObserver.update(section2, null);
		Mockito.verify(consolePrinterMock, Mockito.times(1))
				.printRaiseAlert(Mockito.anyString(), Mockito.anyLong(), Mockito.any(LocalDateTime.class));

		Mockito.verify(consolePrinterMock, Mockito.times(1))
				.printAlertRecovered(Mockito.anyString(), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class));
	}

	@Test
	public void lowTrafficWillNotRaiseAlertNorPrintRecover() {
		Section section = getSectionWithAverageHit("test", 4);
		sectionAlertObserver.update(section, null);
		Section section2 = getSectionWithAverageHit("test2", 3);
		sectionAlertObserver.update(section2, null);
		Mockito.verify(consolePrinterMock, Mockito.never())
				.printRaiseAlert(Mockito.anyString(), Mockito.anyLong(), Mockito.any(LocalDateTime.class));

		Mockito.verify(consolePrinterMock, Mockito.never())
				.printAlertRecovered(Mockito.anyString(), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class));
	}



	@Test
	public void willNotPrintAlertBannerIfNoSectionsInAlert() {
		Section section = getSectionWithAverageHit("test", 4);
		sectionAlertObserver.update(section, null);
		Section section2 = getSectionWithAverageHit("test2", 3);
		sectionAlertObserver.update(section2, null);

		sectionAlertObserver.run();
		Mockito.verify(consolePrinterMock, Mockito.never())
				.printAlertBanner();

		Mockito.verify(consolePrinterMock, Mockito.never())
				.printSectionsInAlert(Mockito.anyMap());
	}

	@Test
	public void willPrintAlertBannerIfSectionsInAlert() {
		Section section = getSectionWithAverageHit("test", 6);
		sectionAlertObserver.update(section, null);
		Section section2 = getSectionWithAverageHit("test2", 38);
		sectionAlertObserver.update(section2, null);

		sectionAlertObserver.run();
		Mockito.verify(consolePrinterMock, Mockito.times(1))
				.printAlertBanner();

		Mockito.verify(consolePrinterMock, Mockito.times(1))
				.printSectionsInAlert(Mockito.anyMap());
	}

	private Section getSectionWithAverageHit(String sectionName, int averageHit) {
		List<Observer> observers = Arrays.asList(new SectionAlertObserver(1, 1, Mockito.mock(ConsolePrinter.class)),
				new SectionStatisticsObserver(2, Mockito.mock(ConsolePrinter.class)));
		Section section = new Section(sectionName, ALERT_INTERVAL, ALERT_THRESHOLD, observers);

		for (int i = 0; i < ALERT_INTERVAL; i++) {
			section.updateHits(averageHit);
		}

		return section;
	}


}