package observer;

import configuration.ConfigurationException;
import console.ConsolePrinter;
import entity.HitStatistics;
import entity.Section;
import entity.SectionHits;

import java.util.*;

public class SectionStatisticsObserver extends TimerTask implements Observer {

	private final int numberOfElements;
	private final ConsolePrinter consolePrinter;
	private final Map<String, Section> sectionMap;

	public SectionStatisticsObserver(int numberOfElements, ConsolePrinter consolePrinter) {
		if(numberOfElements < 1) {
			throw new ConfigurationException("Number of elements can only be strictly positive.");
		}
		this.numberOfElements = numberOfElements;
		this.consolePrinter = consolePrinter;
		this.sectionMap = new HashMap<>();
	}

	@Override
	public void update(Observable o, Object arg) {
		Section updated = (Section) o;
		sectionMap.put(updated.getSectionName(), updated);
	}

	@Override
	public void run() {
		List<SectionHits> allTimeTop = new ArrayList<>();
		List<SectionHits> intervalTop = new ArrayList<>();
		PriorityQueue<SectionHits> allTimeTopSections = new PriorityQueue<>(Comparator.comparingLong(SectionHits::getHits).reversed());
		PriorityQueue<SectionHits> topSectionsDuringInterval = new PriorityQueue<>(Comparator.comparingLong(SectionHits::getHits).reversed());
		for (Section section : sectionMap.values()) {
			HitStatistics hitStatistics = section.getHitStatistics();
			SectionHits allTime = new SectionHits(section.getSectionName(), hitStatistics.getTotalHits());
			SectionHits interval = new SectionHits(section.getSectionName(),
					hitStatistics.getTotalHitsDuringDisplayStatsInterval());
			allTimeTopSections.offer(allTime);
			topSectionsDuringInterval.offer(interval);
		}

		int counter = 0;
		while (counter++ < numberOfElements) {
			if (!allTimeTopSections.isEmpty()) {
				allTimeTop.add(allTimeTopSections.remove());
			}
			if (!topSectionsDuringInterval.isEmpty()) {
				intervalTop.add(topSectionsDuringInterval.remove());
			}
		}

		sectionMap.clear();

		if (!allTimeTop.isEmpty() && !intervalTop.isEmpty()) {
			consolePrinter.printStatsBanner();
			consolePrinter.printIntervalTopSections(intervalTop);
			consolePrinter.printAllTimeTopSections(allTimeTop);
		}


	}


}
