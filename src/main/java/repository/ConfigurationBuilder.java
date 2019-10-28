package repository;

import org.hibernate.cfg.Configuration;

public class ConfigurationBuilder {

	private static final String CONNECTION_URL = "hibernate.connection.url";
	private static final String CONNECTION_PASSWORD = "hibernate.connection.password";
	private static final String CONNECTION_USERNAME = "hibernate.connection.username";
	private static final String SCRIPT_SOURCE = "javax.persistence.sql-load-script-source";

	private String username;
	private String password;
	private String exposedPort;
	private String databaseName;
	private String scriptPath;

	public ConfigurationBuilder withUsername(String username) {
		this.username = username;
		return this;
	}

	public ConfigurationBuilder withPassword(String password) {
		this.password = password;
		return this;
	}

	public ConfigurationBuilder withExposedPort(String port) {
		this.exposedPort = port;
		return this;
	}
	
	public ConfigurationBuilder withDatabaseName(String databaseName) {
		this.databaseName = databaseName;
		return this;
	}
	
	public ConfigurationBuilder withRunningMode(boolean isTestMode) {
		if (isTestMode)
			this.scriptPath = "src/e2e/resources/testLoad.sql";
		else 
			this.scriptPath = "src/main/resources/load.sql";
		return this;
	}

	public Configuration build() {
		Configuration conf = new Configuration().configure();
		conf.setProperty(CONNECTION_USERNAME, this.username);
		conf.setProperty(CONNECTION_PASSWORD, this.password);
		conf.setProperty(CONNECTION_URL, "jdbc:mysql://localhost:" + this.exposedPort + "/" + this.databaseName + "?useSSL=false");
		conf.setProperty(SCRIPT_SOURCE, this.scriptPath);
		return conf;
	}

}
