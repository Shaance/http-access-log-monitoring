package entity;

import java.time.LocalDateTime;

public class AlertInfo {

	private String section;
	private long averageHits;
	private LocalDateTime alertStart;

	public AlertInfo(String section, long averageHits, LocalDateTime alertStart) {
		this.section = section;
		this.averageHits = averageHits;
		this.alertStart = alertStart;
	}

	public String getSection() {
		return section;
	}

	public long getAverageHits() {
		return averageHits;
	}

	public LocalDateTime getAlertStart() {
		return alertStart;
	}

	public void setAverageHits(long averageHits) {
		this.averageHits = averageHits;
	}
}
