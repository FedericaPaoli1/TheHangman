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
	private static Configuration conf;

	private Session session;
	private WordMySQLRepository repo;

	@BeforeClass
	public static void beforeClass() {
		mysql.start();

		conf = new Configuration().configure()
				.setProperty("hibernate.connection.url", mysql.getJdbcUrl() + "?useSSL=false")
				.setProperty("hibernate.connection.username", mysql.getUsername())
				.setProperty("hibernate.connection.password", mysql.getPassword());
		factory = conf.buildSessionFactory();
	}

	@Before
	public void setup() {
		repo = new WordMySQLRepository(factory);

		session = factory.openSession();
		deleteAllRowsFromDatabase();
	}

	private void deleteAllRowsFromDatabase() {
		session.beginTransaction();
		session.createQuery("DELETE FROM Word").executeUpdate();
		session.getTransaction().commit();
	}

	@After
	public void tearDown() {
		session.close();
	}

	@AfterClass
	public static void afterClass() {
		factory.close();
		mysql.stop();
	}

	@Test
	public void testGetRandomWordWhenDatabaseIsEmpty() {
		assertThat(repo.getRandomWord()).isNull();
	}

	@Test
	public void testGetRandomWordWhenDatabaseHasOneRow() {
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
		addWord(1, "test");
		addWord(2, "dog");
		addWord(3, "night");

		for (int i = 0; i < 10; i++)
			assertThat(repo.getRandomWord()).isIn(asList("test", "dog", "night"));
	}

}
