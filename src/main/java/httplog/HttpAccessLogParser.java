package httplog;

import java.util.Optional;

public interface HttpAccessLogParser {

	/**
	 * Log format should be as described here: https://www.w3.org/Daemon/User/Config/Logging.html
	 *
	 * @param httpAccessLog the input string to parse
	 * @return an optional httplog.HttpLogAccessInfo object associated to the string given in input, can contain
	 * no value if the String does not match the expected regexp
	 */
	Optional<HttpLogAccessInfo> parse(String httpAccessLog);
}
