package console;

import entity.AlertInfo;
import entity.SectionHits;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;

public class ConsolePrinterImpl implements ConsolePrinter {

	@Override
	public void printRaiseAlert(String sectionName, long averageHits, LocalDateTime date) {
		System.out.println(String.format("[ALERT - START] High traffic for section %s generated an alert - hits = %d, " +
				"triggered at %s", sectionName, averageHits, date.toString()));
	}

	@Override
	public void printAlertRecovered(String sectionName, LocalDateTime start, LocalDateTime end) {
		long alertDuration = start.until(end, ChronoUnit.SECONDS);
		System.out.println(String.format("[ALERT - END] High traffic alert recovered for section %s at %s." +
				" Alert raised for %s seconds.", sectionName, end, alertDuration));
	}

	@Override
	public void printAlertBanner() {
		printNewLines(2);
		System.out.println("//////////////////////////////////////////////////");
		System.out.println("//////////////  SECTIONS IN ALERT  ///////////////");
		System.out.println("//////////////////////////////////////////////////");
	}

	@Override
	public void printSectionsInAlert(Map<String, AlertInfo> alertInfoMap) {
		LocalDateTime now = LocalDateTime.now();
		for (AlertInfo alertInfo : alertInfoMap.values()) {
			long alertDuration = alertInfo.getAlertStart().until(now, ChronoUnit.SECONDS);
			System.out.println(String.format("Section %s has been in alert for %s seconds" +
					" and was triggered at %s", alertInfo.getSection(), alertDuration, alertInfo.getAlertStart()));
		}
		printNewLines(1);
	}

	@Override
	public void printStatsBanner() {
		printNewLines(1);
		System.out.println("================ STATISTICS ================");
	}

	@Override
	public void printAllTimeTopSections(Collection<SectionHits> topSections) {
		System.out.println("All time top section hits:");
		printTopSections(topSections);
	}

	@Override
	public void printIntervalTopSections(Collection<SectionHits> topSections) {
		System.out.println("Top section hits since last interval:");
		printTopSections(topSections);
	}

	private void printTopSections(Collection<SectionHits> topSections) {
		int counter = 1;
		for (SectionHits p : topSections) {
			System.out.println(String.format("%d. %s - %d", counter++, p.getSectionName(), p.getHits()));
		}
		printNewLines(1);
	}


	private void printNewLines(int number) {
		while (number-- > 0) {
			System.out.println();
		}
	}
}
