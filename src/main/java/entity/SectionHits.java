package entity;

import java.util.Objects;

public class SectionHits{

	private String sectionName;
	private long hits;

	public SectionHits(String sectionName, long hits) {
		this.sectionName = sectionName;
		this.hits = hits;
	}

	public String getSectionName() {
		return sectionName;
	}

	public long getHits() {
		return hits;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SectionHits that = (SectionHits) o;
		return hits == that.hits &&
				sectionName.equals(that.sectionName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sectionName, hits);
	}
}
