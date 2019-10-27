package main;

import static org.assertj.swing.launcher.ApplicationLauncher.*;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MySQLContainer;

import repository.WordMySQLRepository;

@RunWith(GUITestRunner.class)
public class TheHangmanAppE2E extends AssertJSwingJUnitTestCase {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static MySQLContainer mysql = new MySQLContainer("mysql:5.7").withDatabaseName("HangmanDB");

	private static SessionFactory factory;

	private Session session;
	private WordMySQLRepository repo;
	private static Configuration conf;

	private FrameFixture window;

	@BeforeClass
	public static void beforeClass() {
		mysql.start();

		conf = new Configuration().configure()
				.setProperty("hibernate.connection.url", mysql.getJdbcUrl() + "?useSSL=false")
				.setProperty("hibernate.connection.username", mysql.getUsername())
				.setProperty("hibernate.connection.password", mysql.getPassword())
				.setProperty("javax.persistence.sql-load-script-source", "src/e2e/resources/testLoad.sql");

		factory = conf.buildSessionFactory();
	}

	@Override
	protected void onSetUp() {
		session = factory.openSession();

		repo = new WordMySQLRepository(factory);

		application("app.swing.TheHangmanApp").withArgs("--mysql-port=" + mysql.getFirstMappedPort(),
				"--mysql-username=" + mysql.getUsername(), "--mysql-password=" + mysql.getPassword(),
				"--db-name=" + mysql.getDatabaseName(), "-t", "mode=" + "graphical").start();

		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "The Hangman".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());

	}

	@Override
	protected void onTearDown() {
		session.close();
	}

	@Test @GUITest
	public void testCorrectInputChar() {
		setInputAndClick("e");
		window.label(JLabelMatcher.withName("finalWordChar1")).requireText("E");
	}

	@Test @GUITest
	public void testWrongInputChar() {
		setInputAndClick("a");
		for (int i = 0; i < 4; i++) {
			window.label(JLabelMatcher.withName("finalWordChar" + i)).requireText(" ");
		}
		window.textBox("missesTextBox").requireText(" " + 'A');
		window.label(JLabelMatcher.withText("The typed char is not present, please retry.."));
	}
	
	@Test @GUITest
	public void testWinningSequence() {
		String[] inputs = {"t", "e", "s"};
		for (int i = 0; i < inputs.length; i++) {
			setInputAndClick(inputs[i]);
			window.label(JLabelMatcher.withName("finalWordChar" + i)).requireText(inputs[i].toUpperCase());
		}
	}
	
	@Test @GUITest
	public void testLoosingSequence() {
		String[] inputs = {"a", "b", "c", "d", "f", "g"};
		for (int i = 0; i < inputs.length; i++) {
			setInputAndClick(inputs[i]);
			window.textBox("missesTextBox").requireText(".*" + inputs[i].toUpperCase());
			window.label(JLabelMatcher.withText("The typed char is not present, please retry.."));
		}
	}

	private void setInputAndClick(String input) {
		window.textBox("charTextBox").enterText(input);
		window.button(JButtonMatcher.withText("TRY")).click();
	}
}
