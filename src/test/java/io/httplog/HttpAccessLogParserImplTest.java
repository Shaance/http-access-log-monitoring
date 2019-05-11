package io.httplog;

import httplog.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class HttpAccessLogParserImplTest {

	private HttpAccessLogParser httpAccessLogParser;

	@Before
	public void setUp() {
		httpAccessLogParser = new HttpAccessLogParserImpl();
	}

	@Test
	public void parseInvalidFormat() {
		Optional<HttpLogAccessInfo> result = httpAccessLogParser.parse("127.0.0.1 - james " +
				"[09/May/2018:16:00:39 +0000] \"GET /report HTTP/1.0\" ddd 123");

		Assert.assertFalse(result.isPresent());

	}

	@Test(expected = ParsingException.class)
	public void parseInvalidDateFormat() {
		httpAccessLogParser.parse("127.0.0.1 - james [09/May/18:16:00:39 +0000] " +
				"\"GET /report HTTP/1.0\" 200 123");
	}

	@Test
	public void parseLogMissingField() {
		Optional<HttpLogAccessInfo> result = httpAccessLogParser.parse("127.0.0.1 - " +
				"[09/May/18:16:00:39 +0000] \"GET /report HTTP/1.0\" 200 123");
		Assert.assertFalse(result.isPresent());
	}

	@Test(expected = ParsingException.class)
	public void parseLogIncorrectSectionFormat() {
		httpAccessLogParser.parse("127.0.0.1 - james [09/May/2018:16:00:39 +0000] " +
				"\"GET finance HTTP/1.0\" 200 123");
	}

	@Test
	public void parse() {
		Optional<HttpLogAccessInfo> accessInfo = httpAccessLogParser.parse("127.0.0.1 - james " +
				"[09/May/2018:16:00:39 +0000] \"GET /report/finance HTTP/1.0\" 200 123");

		Assert.assertTrue(accessInfo.isPresent());
		Assert.assertEquals(getMockHttpLogAccessInfo(), accessInfo.get());

	}

	private HttpLogAccessInfo getMockHttpLogAccessInfo() {
		LocalDateTime localDateTime = LocalDateTime.parse("09/May/2018:16:00:39 +0000",
				DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss X"));
		return new HttpLogAccessInfo("127.0.0.1", "james",
				localDateTime, HttpRequestType.GET, "/report", 200);
	}
}