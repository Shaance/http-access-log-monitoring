package entity;

public class HitStatistics {

	private long totalHits;
	private long totalHitsDuringAlertInterval;
	private long totalHitsDuringDisplayStatsInterval;

	HitStatistics() {
		this.totalHitsDuringAlertInterval = 0;
		this.totalHitsDuringDisplayStatsInterval = 0;
		this.totalHits = 0;
	}

	public long getTotalHits() {
		return totalHits;
	}

	void incrementTotalHits(long hits) {
		this.totalHits += hits;
	}

	public long getTotalHitsDuringAlertInterval() {
		return totalHitsDuringAlertInterval;
	}

	void incrementTotalHitsDuringAlertInterval(long hits) {
		this.totalHitsDuringAlertInterval += hits;
	}

	public long getTotalHitsDuringDisplayStatsInterval() {
		return totalHitsDuringDisplayStatsInterval;
	}

	void incrementTotalHitsDuringDisplayStatsInterval(long hits) {
		this.totalHitsDuringDisplayStatsInterval += hits;
	}
}
