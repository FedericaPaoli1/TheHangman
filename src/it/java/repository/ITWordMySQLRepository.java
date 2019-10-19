package repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

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
	public static MySQLContainer mysql = new MySQLContainer("mysql:5.7");
	
	private static SessionFactory factory;
	private Session session;
	
	@BeforeClass
	public static void beforeClass()
	{
		mysql.start();

		Configuration conf = new Configuration().configure();
		
		conf.setProperty("hibernate.connection.url", mysql.getJdbcUrl())
			.setProperty("hibernate.connection.username", mysql.getUsername())
			.setProperty("hibernate.connection.password", mysql.getPassword());

		factory = conf.buildSessionFactory();
	}
		
	@Before
	public void setup()
	{
		session = factory.openSession();
		/*
		 * TODO
		 * capire come fare il drop table, cosi' da avere sempre una tabella vuota ad inizio db
		 */
		session.beginTransaction();
		session.createSQLQuery("DELETE FROM Words").executeUpdate();
		session.getTransaction().commit();
	}
	
	@Test
	public void testGetRandomWord()
	{
		addWord("cane");
		addWord("merlo");
		addWord("divisa");
		
		RepositoryInterface rep = new WordMySQLRepository(factory);
		
		assertThat(rep.getRandomWord()).isIn(Arrays.asList("cane", "merlo", "divisa"));
	}

	private void addWord(String wordString)
	{		
		session.beginTransaction();
		Word w = new Word();
		w.setString(wordString);
		session.save(w);
		session.getTransaction().commit();	
	}
	
	@After
	public void teardown()
	{
		session.close();
	}
	
	@AfterClass
	public static void afterClass()
	{
		// closing the container will also close SessionFactory
		mysql.stop();
	}

}
