package manager;

import httplog.HttpAccessLogParser;
import httplog.HttpLogAccessInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParsingManager extends TimerTask {

	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	private HttpAccessLogParser httpAccessLogParser;
	private SectionManager sectionManager;
	private InputStream reader;

	public ParsingManager(HttpAccessLogParser httpAccessLogParser, SectionManager sectionManager, InputStream reader) {
		this.httpAccessLogParser = httpAccessLogParser;
		this.sectionManager = sectionManager;
		this.reader = reader;
	}

	private List<HttpLogAccessInfo> getHttpLogs() throws IOException {

		List<HttpLogAccessInfo> httpLogAccessInfoList = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		while (reader.available() > 0) {
			int read = reader.read();
			//10 == LF / 13 == CR
			if (read != 10 && read != 13) {
				sb.append((char) read);
			} else {
				Optional<HttpLogAccessInfo> parsedLog = httpAccessLogParser.parse(sb.toString());
				parsedLog.ifPresent(httpLogAccessInfoList::add);
				sb.setLength(0);
			}
		}

		return httpLogAccessInfoList;

	}

	@Override
	public void run() {
		try {
			sectionManager.updateSectionHits(getHttpLogs());
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error while reading logs.\n" + e.getMessage());
			System.exit(1);
		}
	}
}
