package configuration;

public class ConfigurationException extends IllegalArgumentException {

	public ConfigurationException(String s) {
		super(s);
	}

	ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
}
