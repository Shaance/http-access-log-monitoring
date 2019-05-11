package httplog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpAccessLogParserImpl implements HttpAccessLogParser {

	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	//127.0.0.1 - james [09/May/2018:16:00:39 +0000] "GET /report HTTP/1.0" 200 123
	private static final String LOG_REGEXP = "^(\\S+) - (\\S+) " +
			"\\[([\\w:/]+\\s[+\\-]\\d{4})] \"(\\S+)" +
			" (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";

	@Override
	public Optional<HttpLogAccessInfo> parse(String httpAccessLog) {
		final Pattern pattern = Pattern.compile(LOG_REGEXP, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(httpAccessLog);
		String remoteHost;
		String user;
		LocalDateTime localDateTime;
		HttpRequestType requestType;
		String section;
		int httpStatusCode;
		HttpLogAccessInfo httpLogAccessInfo = null;

		try {
			if (matcher.find()) {
				remoteHost = matcher.group(1);
				user = matcher.group(2);
				localDateTime = httpLogStringToLocalDateTime(matcher.group(3));
				requestType = HttpRequestType.valueOf(matcher.group(4));
				section = getSectionFromFullPath(matcher.group(5));
				httpStatusCode = Integer.valueOf(matcher.group(7));
				httpLogAccessInfo = new HttpLogAccessInfo(remoteHost, user, localDateTime, requestType, section, httpStatusCode);
			}
			//else the given String did not match the expected format
		} catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | DateTimeParseException ex) {
			LOGGER.log(Level.SEVERE, "Error while parsing the log \n" + ex);
			throw new ParsingException("Could not parse the String " + httpAccessLog + " correctly.");
		}

		return Optional.ofNullable(httpLogAccessInfo);

	}

	private LocalDateTime httpLogStringToLocalDateTime(String date) {
		return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss X"));
	}

	private String getSectionFromFullPath(String path) {
		if (path.equals("/") || path.equals("*")) {
			return path;
		} else {
			String[] split = path.split("/");
			return "/" + split[1];
		}
	}

}
