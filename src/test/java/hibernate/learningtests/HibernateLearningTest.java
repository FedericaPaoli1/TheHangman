package hibernate.learningtests;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MySQLContainer;

public class HibernateLearningTest {

	private static class HibernateUtil {

		private static final Configuration configuration = createConfiguration();

		private static Configuration buildConfiguration() {

			Properties properties = new Properties();
			properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
			properties.put("hibernate.hbm2ddl.auto", "create");
			properties.put("hibernate.show_sql", "true");
			properties.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
			properties.put("hibernate.connection.url", mysql.getJdbcUrl());
			properties.put("hibernate.connection.username", mysql.getUsername());
			properties.put("hibernate.connection.password", mysql.getPassword());
			properties.put("hibernate.c3p0.min_size", 1);
			properties.put("hibernate.c3p0.max_size", 5);
			try {
				return new Configuration().setProperties(properties)
						.addAnnotatedClass(hibernate.learningtests.Word.class);
			} catch (Throwable ex) {
				System.err.println("Initial Configutation creation failed." + ex);
				throw new ExceptionInInitializerError(ex);
			}

		}

		public static Configuration createConfiguration() {
			return configuration;
		}
	}

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static MySQLContainer mysql = new MySQLContainer("mysql:5.7").withDatabaseName("TestDB");

	private SessionFactory factory;
	private Session session;
	private static Configuration conf;

	@BeforeClass
	public static void beforeClass() {
		conf = HibernateUtil.buildConfiguration();
	}

	@Before
	public void setup() {
		factory = conf.buildSessionFactory();
		session = factory.openSession();
	}

	@Test
	public void create() {
		session.beginTransaction();

		Word firstTestWord = new Word(1, "firstTest");
		Word secondTestWord = new Word(2, "secondTest");

		Integer firstTestWordId = (Integer) session.save(firstTestWord);
		Integer secondTestWordId = (Integer) session.save(secondTestWord);

		Word firstTestWordFromDb = (Word) session.get(Word.class, firstTestWordId);
		Word secondTestWordFromDb = (Word) session.get(Word.class, secondTestWordId);

		assertThat(firstTestWord).isEqualTo(firstTestWordFromDb);
		assertThat(secondTestWord).isEqualTo(secondTestWordFromDb);
		assertThat(firstTestWordFromDb).isNotEqualTo(secondTestWordFromDb);

		session.getTransaction().rollback();
		session.close();
	}

	@Test
	public void update() {
		session.beginTransaction();

		Word firstTestWord = new Word(1, "firstTest");
		Integer firstTestWordId = (Integer) session.save(firstTestWord);

		Word firstTestWordFromDb = (Word) session.get(Word.class, firstTestWordId);

		String newString = "firstUpdatedTest";
		firstTestWord.setString(newString);
		session.update(firstTestWord);

		Word firstTestWordFromDbAfterUpdate = (Word) session.get(Word.class, firstTestWordId);

		assertThat(firstTestWord).isEqualTo(firstTestWordFromDb);
		assertThat(firstTestWordFromDb).isEqualTo(firstTestWordFromDbAfterUpdate);
		assertThat(firstTestWord).isEqualTo(firstTestWordFromDbAfterUpdate);
		assertThat(newString).isEqualTo(firstTestWord.getString());

		session.getTransaction().rollback();
		session.close();
	}

	@Test
	public void delete() {
		session.beginTransaction();

		Word firstTestWord = new Word(1, "firstTest");
		Integer firstTestWordId = (Integer) session.save(firstTestWord);

		session.delete(firstTestWord);

		Word notExistingWord = (Word) session.get(Word.class, firstTestWordId);

		assertThat(notExistingWord).isNull();

		session.getTransaction().rollback();
		session.close();
	}

	@Test
	public void query() {

		Word firstTestWord = new Word(1, "firstTest");
		Word secondTestWord = new Word(2, "secondTest");

		session.beginTransaction();
		session.save(firstTestWord);
		session.save(secondTestWord);
		session.getTransaction().commit();

		Query<Word> q = session.createQuery("select w from Word w", Word.class);
		List<Word> wordsFromDb = q.list();

		assertThat(wordsFromDb).contains(firstTestWord, secondTestWord);

		Long wordsNumberFromDb = session.createQuery("SELECT COUNT(w) FROM Word w", Long.class).uniqueResult();

		assertThat(wordsNumberFromDb).isEqualTo(2);

		session.close();
	}

	@Test
	public void fillDatabaseOnStartup() {
		Configuration cf = HibernateUtil.buildConfiguration().setProperty(
				"javax.persistence.sql-load-script-source",
				"src/test/resources/testLoad.sql");

		Session s = cf.buildSessionFactory().openSession();

		Long wordsNumberFromDb = session.createQuery("SELECT COUNT(w) FROM Word w", Long.class).uniqueResult();

		assertThat(wordsNumberFromDb).isGreaterThanOrEqualTo(1);

		s.close();
	}

}
