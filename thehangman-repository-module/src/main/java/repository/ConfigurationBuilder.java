package repository;

import org.hibernate.cfg.Configuration;

public class ConfigurationBuilder {

	private static final String CONNECTION_URL = "hibernate.connection.url";
	private static final String CONNECTION_LOGIN = "hibernate.connection.password";
	private static final String CONNECTION_USERNAME = "hibernate.connection.username";

	private String username;
	private String password;
	private String exposedPort;
	private String databaseName;

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
	
	public Configuration build() {
		Configuration conf = new Configuration().configure();
		conf.setProperty(CONNECTION_USERNAME, this.username);
		conf.setProperty(CONNECTION_LOGIN, this.password);
		conf.setProperty(CONNECTION_URL, "jdbc:mysql://localhost:" + this.exposedPort + "/" + this.databaseName + "?useSSL=false");
		return conf;
	}

}
