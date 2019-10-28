package repository;

import static org.assertj.core.api.Assertions.*;

import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import repository.ConfigurationBuilder;

public class TestConfigurationBuilder {

	private static final String USERNAME = "name";
	private static final String PASSWORD = "test";
	private static final String PORT = "000";
	private ConfigurationBuilder builder;

	@Before
	public void setup() {
		builder = new ConfigurationBuilder();
	}

	@Test
	public void testBuildSetsAllParameters() {
		builder.withExposedPort(PORT).withPassword(PASSWORD).withUsername(USERNAME);

		Configuration conf = builder.build();

		assertThat(conf.getProperty("hibernate.connection.username")).isEqualTo(USERNAME);
		assertThat(conf.getProperty("hibernate.connection.password")).isEqualTo(PASSWORD);
		assertThat(conf.getProperty("hibernate.connection.url")).containsPattern(".*" + PORT + ".*");
	}

}
