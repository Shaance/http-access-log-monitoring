import configuration.Configuration;
import configuration.ConfigurationException;
import configuration.ConfigurationManager;
import configuration.ConfigurationParserImpl;
import console.ConsolePrinterImpl;
import httplog.HttpAccessLogParserImpl;
import manager.ParsingManager;
import manager.SectionManager;
import observer.SectionAlertObserver;
import observer.SectionStatisticsObserver;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Observer;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Datadog homework - http log monitoring console program
 * <p>
 * Display stats every 10s about the traffic during those 10s: the sections of the web site with the most hits,
 * as well as interesting summary manager on the traffic as a whole. A section is defined as being what's before
 * the second '/' in the resource section of the log line. For example, the section for "/pages/create" is "/pages"
 * <p>
 * Make sure a user can keep the app running and monitor the log file continuously
 * <p>
 * Whenever total traffic for the past 2 minutes exceeds a certain number on average, add a message saying that
 * “High traffic generated an alert - hits = {value}, triggered at {time}”. The default threshold should be 10
 * requests per second, and should be overridable.
 * <p>
 * Whenever the total traffic drops again below that value on average for the past 2 minutes, add another message
 * detailing when the alert recovered.
 * <p>
 * Make sure all messages showing when alerting thresholds are crossed remain visible on the page for historical reasons.
 * <p>
 * Write a test for the alerting logic.
 * <p>
 * Explain how you’d improve on this application design.
 */
public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	/**
	 * @param args defines the program arguments which will be used as configuration for the application.
	 *             You can define multiple option in the following form (-[option] [value] )*
	 *             e.g -R 10 -AT 20
	 *
	 *             <p>
	 *             -P: specifies the path of the log file (/tmp/access.log by default)
	 *             -R: specifies the stats display rate in seconds (10s by default)
	 *             -AT: specifies the average section hits above which an alert should be emitted (10 by default)
	 *             -AW: specifies the alert window duration in seconds on which the average section hits is
	 *             calculated upon (120s by default)
	 *             <p>
	 *             An exception will be raised if an option is unknown or if the associated value is in an
	 *             incorrect format / is missing
	 */
	public static void main(String[] args) {
		ConfigurationManager configurationManager = new ConfigurationManager(new ConfigurationParserImpl());
		Configuration configuration = configurationManager.getConfiguration(args);
		Timer timer = new Timer();
		ConsolePrinterImpl consolePrinter = new ConsolePrinterImpl();

		try {

			SectionAlertObserver sectionAlertObserver = new SectionAlertObserver(
					configuration.getAlertWindowDuration(),
					configuration.getAlertThreshold(),
					consolePrinter
			);

			SectionStatisticsObserver statisticsObserver = new SectionStatisticsObserver(5, consolePrinter);
			List<Observer> observers = Arrays.asList(sectionAlertObserver, statisticsObserver);
			SectionManager sectionManager = new SectionManager(configuration, observers);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(configuration.getLogFilePath()));
			ParsingManager parsingManager = new ParsingManager(new HttpAccessLogParserImpl(), sectionManager, bufferedInputStream);

			timer.scheduleAtFixedRate(parsingManager, 0, 1_000);
			timer.scheduleAtFixedRate(sectionAlertObserver, 0, configuration.getStatsDisplayRefreshRate() * 1_000);
			timer.scheduleAtFixedRate(statisticsObserver, 50, configuration.getStatsDisplayRefreshRate() * 1_000);
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "Log file not found. " + e.getMessage());
			System.exit(1);
		} catch (ConfigurationException e) {
			LOGGER.log(Level.SEVERE, "Error in configuration. " + e.getMessage());
			System.exit(2);
		}

	}
}
