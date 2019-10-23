package repository;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MySQLContainer;

public class ITWordMySQLRepository {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static MySQLContainer mysql = new MySQLContainer("mysql:5.7").withDatabaseName("HangmanDB");

	private static SessionFactory factory;

	private Session session;
	private WordMySQLRepository repo;
	private static Configuration conf;

	@BeforeClass
	public static void beforeClass() {
		mysql.start();

		conf = new Configuration().configure();

		conf.setProperty("hibernate.connection.url", mysql.getJdbcUrl() + "?useSSL=false")
				.setProperty("hibernate.connection.username", mysql.getUsername())
				.setProperty("hibernate.connection.password", mysql.getPassword());
	}

	@Before
	public void setup() {
		factory = conf.buildSessionFactory();
		session = factory.openSession();

		repo = new WordMySQLRepository(factory);
	}

	@Test
	public void testDatabaseContainsElementsWhenCreated() {
		session.beginTransaction();
		Long rows = (Long) session.createQuery("SELECT COUNT(*) FROM Word").uniqueResult();
		session.getTransaction().commit();

		assertThat(rows).isGreaterThanOrEqualTo(1);
	}

	@Test
	public void testGetRandomWordWhenDatabaseIsEmpty() {
		deleteAllRowsFromDatabase();

		assertThat(repo.getRandomWord()).isNull();
	}

	private void deleteAllRowsFromDatabase() {
		session.beginTransaction();
		session.createSQLQuery("DELETE FROM Words").executeUpdate();
		session.getTransaction().commit();
	}

	@Test
	public void testGetRandomWordWhenDatabaseHasOneRow() {
		deleteAllRowsFromDatabase();
		addWord(1, "test");

		assertThat(repo.getRandomWord()).isEqualTo("test");
	}

	private void addWord(int id, String wordString) {
		session.beginTransaction();

		Word w = new Word();
		w.setId(id);
		w.setString(wordString);

		session.save(w);
		session.getTransaction().commit();
	}

	@Test
	public void testGetRandomWordWhenDatabaseHasSeveralRows() {
		deleteAllRowsFromDatabase();
		addWord(1, "test");
		addWord(2, "dog");
		addWord(3, "night");

		for (int i = 0; i < 10; i++)
			assertThat(repo.getRandomWord()).isIn(asList("test", "dog", "night"));
	}

	@After
	public void teardown() {
		session.close();
	}

	@AfterClass
	public static void afterClass() {
		factory.close();
		mysql.stop();
	}

}
