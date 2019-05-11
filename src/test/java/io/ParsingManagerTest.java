package io;

import httplog.HttpAccessLogParser;
import httplog.HttpAccessLogParserImpl;
import manager.ParsingManager;
import manager.SectionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class ParsingManagerTest {

	private HttpAccessLogParser httpAccessLogParser;
	private SectionManager sectionManager;

	private ParsingManager parsingManager;

	@Before
	public void setUp() {
		this.httpAccessLogParser = Mockito.mock(HttpAccessLogParserImpl.class);
		this.sectionManager = Mockito.mock(SectionManager.class);
		InputStream reader = getClass().getClassLoader().getResourceAsStream("http_access.log");
		this.parsingManager = new ParsingManager(httpAccessLogParser, sectionManager, reader);
	}

	@Test
	public void emptyHttpLogListIfParsedLogOptionalIsNotPresent() {
		Mockito.when(httpAccessLogParser.parse(Mockito.anyString())).thenReturn(Optional.empty());
		parsingManager.run();
		ArgumentCaptor<List> logListInterceptor = ArgumentCaptor.forClass(List.class);
		Mockito.verify(sectionManager, Mockito.times(1)).updateSectionHits(logListInterceptor.capture());
		List logList = logListInterceptor.getValue();
		Assert.assertTrue(logList.isEmpty());
	}

	@Test
	public void correctNumberOfLogsCaptured() {
		Mockito.when(httpAccessLogParser.parse(Mockito.anyString())).thenCallRealMethod();
		parsingManager.run();
		ArgumentCaptor<List> logListInterceptor = ArgumentCaptor.forClass(List.class);
		Mockito.verify(sectionManager, Mockito.times(1)).updateSectionHits(logListInterceptor.capture());
		List logList = logListInterceptor.getValue();
		Assert.assertEquals(15, logList.size());
	}


}