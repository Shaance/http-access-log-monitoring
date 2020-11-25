package httplog;

import java.time.LocalDateTime;
import java.util.Objects;

public class HttpLogAccessInfo {

	private final String remoteHost;
	private final String remoteUser;
	private final LocalDateTime localDateTime;
	private final HttpRequestType requestType;
	private final String section;
	private final int httpStatusCode;


	public HttpLogAccessInfo(String remoteHost, String remoteUser, LocalDateTime localDateTime,
	                  HttpRequestType requestType, String section, int httpStatusCode) {

		this.remoteHost = remoteHost;
		this.remoteUser = remoteUser;
		this.localDateTime = localDateTime;
		this.requestType = requestType;
		this.section = section;
		this.httpStatusCode = httpStatusCode;
	}


	public String getRemoteHost() {
		return remoteHost;
	}

	public String getRemoteUser() {
		return remoteUser;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public HttpRequestType getRequestType() {
		return requestType;
	}

	public String getSection() {
		return section;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}


	@Override
	public String toString() {
		return "HttpLogAccessInfo{" +
				"remoteHost='" + remoteHost + '\'' +
				", remoteUser='" + remoteUser + '\'' +
				", localDateTime=" + localDateTime +
				", requestType=" + requestType +
				", section='" + section + '\'' +
				", httpStatusCode=" + httpStatusCode +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HttpLogAccessInfo that = (HttpLogAccessInfo) o;
		return httpStatusCode == that.httpStatusCode &&
				remoteHost.equals(that.remoteHost) &&
				remoteUser.equals(that.remoteUser) &&
				localDateTime.equals(that.localDateTime) &&
				requestType == that.requestType &&
				section.equals(that.section);
	}

	@Override
	public int hashCode() {
		return Objects.hash(remoteHost, remoteUser, localDateTime, requestType, section, httpStatusCode);
	}
}
