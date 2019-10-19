package repository;

import java.util.Random;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class WordMySQLRepository implements RepositoryInterface {

	private SessionFactory factory;
	
	public WordMySQLRepository(SessionFactory factory) 
	{
		this.factory = factory;
	}
	
	@Override
	public String getRandomWord() 
	{
		Session session = factory.openSession();
		
		session.beginTransaction();
		
		String sql = "SELECT COUNT(*) FROM Word";
		
		Long numberOfRows = (Long) session.createQuery(sql).uniqueResult();
		Word word = session.get(Word.class, new Random().nextInt(numberOfRows.intValue()) + 1);
		session.getTransaction().commit();
		
		session.close();
		return word.getString();
	}

	
//	public static void main(String[] args)
//	{
//		WordMySQLRepository rep = new WordMySQLRepository(
//				new Configuration()
//					.configure()
//					.buildSessionFactory());
//		System.out.println(rep.getRandomWord());
//	}

//	
//	public static void main(String[] args) {
//		WordMySQLRepository rep = new WordMySQLRepository();		
//		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("import.sql");
//
//		BufferedReader reader = new BufferedReader(
//				new InputStreamReader(in)
//				);
//		try {
//			rep.executeStatement(reader, rep.entityManager);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println(rep.getRandomWord());
//	}

}
