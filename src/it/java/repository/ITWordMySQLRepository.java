package repository;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

public class ITWordMySQLRepository {
	
//	@SuppressWarnings("rawtypes")
//	@ClassRule
//	public static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:9")
//						.withDatabaseName("HangmanDB")
//						.withUsername("root")
//						.withPassword("root");
	  
	Connection conn;
	
//	@Before
//	public void setup()
//	{
//		try {
//			conn = DriverManager.getConnection(postgres.getContainerIpAddress());
//			System.out.println("Connection success!");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//	
	@Test
	public void test() {
		assertTrue(true);
	}

}
