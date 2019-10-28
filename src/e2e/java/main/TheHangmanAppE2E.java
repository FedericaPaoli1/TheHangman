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
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MySQLContainer;

@RunWith(GUITestRunner.class)
public class TheHangmanAppE2E extends AssertJSwingJUnitTestCase {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static MySQLContainer mysql = new MySQLContainer("mysql:5.7").withDatabaseName("HangmanDB");

	private FrameFixture window;

	@BeforeClass
	public static void beforeClass() {
		mysql.start();
	}

	@Override
	protected void onSetUp() {

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

	@Test
	@GUITest
	public void testCorrectInputChar() {
		setInputAndClick("e");
		window.label(JLabelMatcher.withName("finalWordChar1")).requireText("E");
	}

	@Test
	@GUITest
	public void testWrongInputChar() {
		setInputAndClick("a");
		for (int i = 0; i < 4; i++) {
			window.label(JLabelMatcher.withName("finalWordChar" + i)).requireText(" ");
		}
		window.textBox("missesTextBox").requireText(" " + 'A');
		window.label(JLabelMatcher.withText("The typed char is not present, please retry.."));
	}

	@Test
	@GUITest
	public void testWinningSequence() {
		String[] inputs = { "t", "e", "s" };
		for (int i = 0; i < inputs.length; i++) {
			setInputAndClick(inputs[i]);
			window.label(JLabelMatcher.withName("finalWordChar" + i)).requireText(inputs[i].toUpperCase());
		}
	}

	@Test
	@GUITest
	public void testLoosingSequence() {
		String[] inputs = { "a", "b", "c", "d", "f", "g" };
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
