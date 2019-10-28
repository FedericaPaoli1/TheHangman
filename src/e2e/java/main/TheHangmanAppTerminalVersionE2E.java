package main;

import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MySQLContainer;

import ui.TerminalUI;

public class TheHangmanAppTerminalVersionE2E {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static MySQLContainer mysql = new MySQLContainer("mysql:5.7").withDatabaseName("HangmanDB");

	private static final PrintStream OLD_OUT = System.out;
	private static final InputStream OLD_IN = System.in;

	private ByteArrayOutputStream out;
	
	@Before
	public void setup() {
		out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
	}
	
	@After
	public void tearDown() {
		System.setOut(OLD_OUT);
		System.setIn(OLD_IN);
	}
	
	@Test
	public void testWinningSequence() {
		System.setIn(generateInputSequence("t e s"));

		startApp();
		
		assertThat(out.toString())
			.contains("_ _ _ _")
			.contains("T _ _ T")
			.contains("T E _ T")
			.contains("T E S T");
		
		assertThat(out.toString())
			.contains(TerminalUI.WINNING_MESSAGE);
	}

	private ByteArrayInputStream generateInputSequence(String sequence) {
		return new ByteArrayInputStream(sequence.replace(" ", "\n").getBytes());
	}

	private void startApp() {
		application("app.swing.TheHangmanApp").withArgs(
				"--mysql-port=" + mysql.getFirstMappedPort(),
				"--mysql-username=" + mysql.getUsername(), 
				"--mysql-password=" + mysql.getPassword(),
				"--db-name=" + mysql.getDatabaseName(), 
				"-t", 
				"mode=" + "terminal").start();
	}
	
	@Test
	public void testLoosingMessage() {
		System.setIn(generateInputSequence("a b b c d f g"));
		
		startApp();
		
		assertThat(out.toString())
			.doesNotContain("T E S T")
			.contains("The typed char is not present, please retry..")
			.contains("Already typed char, please retry..")
			.contains("[A, B, C, D, F, G]")
			.contains(TerminalUI.LOOSING_MESSAGE);
	}
		
}
