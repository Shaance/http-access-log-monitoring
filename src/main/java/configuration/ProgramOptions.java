package configuration;

import java.util.HashMap;
import java.util.Map;

public enum ProgramOptions {

	PATH_OPTION("P"), STATS_DISPLAY_RATE_OPTION("R"), ALERT_THRESHOLD_OPTION("AT"),
	ALERT_WINDOW_OPTION("AW");

	private static final Map<String, ProgramOptions> BY_LABEL = new HashMap<>();

	static {
		for (ProgramOptions p : values()) {
			BY_LABEL.put(p.label, p);
		}
	}

	public final String label;

	ProgramOptions(String label) {
		this.label = label;
	}

	public static ProgramOptions valueOfLabel(String label) {
		return BY_LABEL.get(label);
	}


}
