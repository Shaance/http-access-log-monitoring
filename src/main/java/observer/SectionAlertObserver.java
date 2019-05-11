package observer;

import console.ConsolePrinter;
import entity.AlertInfo;
import entity.HitStatistics;
import entity.Section;

import java.time.LocalDateTime;
import java.util.*;

public class SectionAlertObserver extends TimerTask implements Observer {

	private final int alertInterval;
	private final int alertThreshold;
	private final ConsolePrinter consolePrinter;
	private final Map<String, AlertInfo> sectionsInAlert;


	public SectionAlertObserver(int alertInterval, int alertThreshold, ConsolePrinter consolePrinter) {
		this.alertInterval = alertInterval;
		this.alertThreshold = alertThreshold;
		this.consolePrinter = consolePrinter;
		this.sectionsInAlert = new HashMap<>();
	}

	@Override
	public void update(Observable o, Object arg) {
		Section updated = (Section) o;
		HitStatistics hitStatistics = updated.getHitStatistics();
		String sectionName = updated.getSectionName();
		long averageHits = hitStatistics.getTotalHitsDuringAlertInterval() / alertInterval;

		if (!sectionsInAlert.containsKey(sectionName) && averageHits > alertThreshold) {
			LocalDateTime alertTime = LocalDateTime.now();
			consolePrinter.printRaiseAlert(sectionName, averageHits, alertTime);
			sectionsInAlert.put(sectionName, new AlertInfo(sectionName, averageHits, alertTime));
		} else if (sectionsInAlert.containsKey(sectionName)) {
			AlertInfo alertInfo = sectionsInAlert.get(sectionName);
			if (averageHits < alertThreshold) {
				sectionsInAlert.remove(sectionName);
				consolePrinter.printAlertRecovered(sectionName, alertInfo.getAlertStart(), LocalDateTime.now());
			} else {
				alertInfo.setAverageHits(averageHits);
			}
		}

	}


	@Override
	public void run() {
		if (!sectionsInAlert.isEmpty()) {
			consolePrinter.printAlertBanner();
			consolePrinter.printSectionsInAlert(sectionsInAlert);
		}
	}
}
