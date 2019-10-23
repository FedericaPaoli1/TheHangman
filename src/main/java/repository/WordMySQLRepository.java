package repository;

import java.util.Random;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class WordMySQLRepository implements RepositoryInterface {

	private static final String COUNT_ALL_WORDS = "SELECT COUNT(*) FROM Word";
	private static final Random GENERATOR = new Random();
	private SessionFactory factory;

	public WordMySQLRepository(SessionFactory factory) {
		this.factory = factory;
	}

	@Override
	public String getRandomWord() {
		String string = null;

		Session session = factory.getCurrentSession();
		session.beginTransaction();

		Long numberOfRows = (Long) session.createQuery(COUNT_ALL_WORDS).uniqueResult();

		if (numberOfRows > 0) {
			Word word = session.get(Word.class, GENERATOR.nextInt(numberOfRows.intValue()) + 1);
			session.getTransaction().commit();
			string = word.getString();
		} else {
			session.getTransaction().rollback();
		}

		return string;
	}
}
