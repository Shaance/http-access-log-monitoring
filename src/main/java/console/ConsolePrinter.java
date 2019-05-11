package console;

import entity.AlertInfo;
import entity.SectionHits;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

public interface ConsolePrinter {

	/**
	 * Prints an alert message on console with the following parameters
	 *
	 * @param sectionName the section for which the alert is raised
	 * @param averageHits the average hits for which it surpassed the alert threshold
	 * @param date        the date the alert was raised
	 */
	void printRaiseAlert(String sectionName, long averageHits, LocalDateTime date);

	/**
	 * Prints an alert recover message on console with the following parameters
	 *
	 * @param sectionName the section for which the alert is over
	 * @param start       the date the alert was raised
	 * @param end         the date the alert is over
	 */
	void printAlertRecovered(String sectionName, LocalDateTime start, LocalDateTime end);

	/**
	 * Prints a alert banner
	 */
	void printAlertBanner();


	/**
	 * Prints the sections which are currently in alert status
	 *
	 * @param alertInfoMap the section which its associated alert information
	 */
	void printSectionsInAlert(Map<String, AlertInfo> alertInfoMap);

	/**
	 * Print a statistics banner
	 */
	void printStatsBanner();

	/**
	 * Prints all time top sections
	 *
	 * @param topSections the collection of sections
	 */
	void printAllTimeTopSections(Collection<SectionHits> topSections);

	/**
	 * Prints top sections during the configured statistics display interval
	 *
	 * @param topSections the collection of sections
	 */
	void printIntervalTopSections(Collection<SectionHits> topSections);

}
