package entity;

import java.util.*;

public class Section extends Observable {

	private final String sectionName;
	private final HitStatistics hitStatistics;
	private final Queue<Long> hitsInAlertInterval;
	private final Queue<Long> hitsInDisplayStatsInterval;

	public Section(String sectionName, int alertInterval, int displayStatsInterval, Collection<Observer> observers) {

		this.sectionName = sectionName;
		this.hitStatistics = new HitStatistics();
		this.hitsInAlertInterval = new ArrayDeque<>(alertInterval);
		this.hitsInDisplayStatsInterval = new ArrayDeque<>(displayStatsInterval);
		initializeQueue(hitsInAlertInterval, alertInterval);
		initializeQueue(hitsInDisplayStatsInterval, displayStatsInterval);

		observers.forEach(this::addObserver);
	}

	public void updateHits(long hits) {
		hitStatistics.incrementTotalHits(hits);

		assert !hitsInDisplayStatsInterval.isEmpty();
		hitStatistics.incrementTotalHitsDuringDisplayStatsInterval(-hitsInDisplayStatsInterval.remove() + hits);
		hitsInDisplayStatsInterval.offer(hits);

		assert !hitsInAlertInterval.isEmpty();
		hitStatistics.incrementTotalHitsDuringAlertInterval(-hitsInAlertInterval.remove() + hits);
		hitsInAlertInterval.offer(hits);

		setChanged();
		notifyObservers();
	}

	public String getSectionName() {
		return sectionName;
	}

	public HitStatistics getHitStatistics() {
		return hitStatistics;
	}

	private void initializeQueue(Queue<Long> queue, long length) {
		while (length-- > 0) {
			queue.add(0L);
		}
	}
}
