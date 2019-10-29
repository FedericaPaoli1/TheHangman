package hibernate.learningtests;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MySQLContainer;


public class HibernateLearningTest {

//	@Entity
//	@Table(name = "Words")
//	public class Word {
//
//		private Integer id;
//		private String string;
//
//		public Word(int id, String string) {
//			this.id = id;
//			this.string = string;
//		}
//
//		@Id
//		@Column(name = "word_id")
//		public Integer getId() {
//			return id;
//		}
//
//		@Column(name = "word_string")
//		public String getString() {
//			return string;
//		}
//
//		public void setId(Integer id) {
//			this.id = id;
//		}
//
//		public void setString(String string) {
//			this.string = string;
//		}
//
//	}

	private static class HibernateUtil {

		private static final SessionFactory sessionFactory = buildSessionFactory();

		private static SessionFactory buildSessionFactory() {

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
						.addAnnotatedClass(hibernate.learningtests.Word.class)
						.buildSessionFactory(
								new StandardServiceRegistryBuilder().applySettings(properties).build()
								);
			} catch (Throwable ex) {
				System.err.println("Initial SessionFactory creation failed." + ex);
				throw new ExceptionInInitializerError(ex);
			}

		}

		public static SessionFactory getSessionFactory() {
			return sessionFactory;
		}
	}

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static MySQLContainer mysql = new MySQLContainer("mysql:5.7").withDatabaseName("TestDB");

	private Session session;

	@Before
	public void setup() {
		session = HibernateUtil.getSessionFactory().openSession();
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

		wordsFromDb.forEach(w -> System.out.println(w.getClass().getName()));

		Long wordsNumberFromDb = session.createQuery("SELECT COUNT(w) FROM Word w", Long.class).uniqueResult();

		assertThat(wordsFromDb).contains(firstTestWord, secondTestWord);
		assertThat(wordsNumberFromDb).isEqualTo(2);

		session.close();
	}

}
