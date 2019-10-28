package main;

import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MySQLContainer;

public class TheHangmanAppTerminalVersionE2E {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static MySQLContainer mysql = new MySQLContainer("mysql:5.7").withDatabaseName("HangmanDB");
	
	@Test
	public void testCorrectInputChar() {
		System.setIn(new ByteArrayInputStream("e".getBytes()));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		startApp();
		System.out.println(out.toString());
		assertThat(out.toString()).contains("_ E _ _");
		
	}
	
	private void startApp() {
		
		application("app.swing.TheHangmanApp").withArgs(
				"--mysql-port=" + mysql.getFirstMappedPort(),
				"--mysql-username=" + mysql.getUsername(), 
				"--mysql-password=" + mysql.getPassword(),
				"--db-name=" + mysql.getDatabaseName(), 
				"-t", 
				"--mode=" + "terminal").start();

	}

	
	
	
}
