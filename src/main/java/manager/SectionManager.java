package manager;

import configuration.Configuration;
import configuration.ConfigurationException;
import entity.Section;
import httplog.HttpLogAccessInfo;

import java.util.*;
import java.util.stream.Collectors;

public class SectionManager {
	private final Map<String, Section> sectionMap;
	private final Collection<Observer> observers;
	private final int alertInterval;
	private final int displayStatsInterval;

	public SectionManager(Configuration configuration, Collection<Observer> observers) throws ConfigurationException {
		if (configuration.getAlertWindowDuration() < 1 || configuration.getStatsDisplayRefreshRate() < 1) {
			throw new ConfigurationException("The intervals in configuration should be strictly positive numbers.");
		}
		this.alertInterval = configuration.getAlertWindowDuration();
		this.displayStatsInterval = configuration.getStatsDisplayRefreshRate();
		this.sectionMap = new HashMap<>();
		this.observers = observers;
	}

	public void updateSectionHits(List<HttpLogAccessInfo> logAccessInfoList) {

		if (logAccessInfoList != null) {
			Map<String, Long> hitsBySection = logAccessInfoList.stream()
					.collect(Collectors.groupingBy(HttpLogAccessInfo::getSection, Collectors.counting()));

			for (Map.Entry<String, Section> entry : sectionMap.entrySet()) {
				if (!hitsBySection.containsKey(entry.getKey())) {
					entry.getValue().updateHits(0);
				} else {
					entry.getValue().updateHits(hitsBySection.get(entry.getKey()));
				}
			}

			for (Map.Entry<String, Long> entry : hitsBySection.entrySet()) {
				String section = entry.getKey();
				if (!sectionMap.containsKey(section)) {
					sectionMap.put(section, new Section(section, alertInterval, displayStatsInterval, observers));
				}
			}
		}
	}
}
