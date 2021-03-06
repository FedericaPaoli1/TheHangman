package ui;

import static org.assertj.core.api.Assertions.*;

import javax.swing.ImageIcon;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

import exceptions.AlreadyTypedException;
import exceptions.CharAbsenceException;

@RunWith(GUITestRunner.class)
public class TestGraphicalUI extends AssertJSwingJUnitTestCase {

	private static final int GUESSING_WORD_LENGTH = 4;
	private FrameFixture window;
	private GraphicalUI gui;

	@Override
	protected void onSetUp() {
		GuiActionRunner.execute(() -> {
			gui = new GraphicalUI(4);
			return gui;
		});
		window = new FrameFixture(robot(), gui);

		window.show();
	}

	@Test
	public void testControlsInitialStates() {
		assertThat(window.label(JLabelMatcher.withName("gameResult")).target().getText()).isEqualTo(" ");
		assertThat(window.label(JLabelMatcher.withName("image")).target().isShowing()).isTrue();
		for (int i = 0; i < GUESSING_WORD_LENGTH; i++) {
			window.label(JLabelMatcher.withName("finalWordChar" + i));
		}
		assertThat(window.button(JButtonMatcher.withText("TRY")).target().isEnabled()).isFalse();
		assertThat(window.textBox("charTextBox").target().isEditable()).isTrue();
		assertThat(window.textBox("charTextBox").target().isEnabled()).isTrue();
		assertThat(window.label(JLabelMatcher.withText("MISSES: ")).target().isShowing()).isTrue();
		assertThat(window.textBox("missesTextBox").target().isEditable()).isFalse();
	}

	@Test
	@GUITest
	public void testWhenCharIsTypedThenTryButtonShouldBeEnabled() {
		window.textBox("charTextBox").enterText("e");
		assertThat(window.button(JButtonMatcher.withText("TRY")).target().isEnabled()).isTrue();
	}

	@Test
	@GUITest
	public void testWhenWhiteSpacesAreTypedThenTryButtonShouldBeDisabled() {
		window.textBox("charTextBox").enterText("   ");
		assertThat(window.button(JButtonMatcher.withText("TRY")).target().isEnabled()).isFalse();
	}

	@Test
	@GUITest
	public void testWhenWhiteSpacesAreFollowedByACharThenTryButtonShouldBeEnabled() {
		window.textBox("charTextBox").enterText("   c");
		assertThat(window.button(JButtonMatcher.withText("TRY")).target().isEnabled()).isTrue();
	}

	@Test
	@GUITest
	public void testGetInputCharWhenCharIsTyped() {
		window.textBox("charTextBox").enterText("e");
		window.button(JButtonMatcher.withText("TRY")).click();

		assertThat(gui.getInputChar()).isEqualTo('e');
	}

	@Test
	@GUITest
	public void testGetInputCharWhenAnotherCharIsTyped() {
		window.textBox("charTextBox").enterText("a");
		window.button(JButtonMatcher.withText("TRY")).click();

		assertThat(gui.getInputChar()).isEqualTo('a');
	}

	@Test
	@GUITest
	public void testGetInputCharWhenSeveralCharsAreTyped() {
		JTextComponentFixture charTextBox = window.textBox("charTextBox");
		charTextBox.enterText("abc");
		window.button(JButtonMatcher.withText("TRY")).click();

		assertThat(gui.getInputChar()).isEqualTo('a');

		charTextBox.setText("");

		charTextBox.enterText(" abc");
		window.button(JButtonMatcher.withText("TRY")).click();

		assertThat(gui.getInputChar()).isEqualTo('a');
	}

	@Test
	public void testGetInputCharWhenIllegalStateExceptionIsThrown() {
		gui.getQueue().clear();
		
		Thread t = new Thread(() -> {
			Thread.currentThread().interrupt();

			assertThatThrownBy(() -> gui.getInputChar()).isInstanceOf(IllegalStateException.class);
		});
		
		t.start();	
	}

	@Test
	@GUITest
	public void testIsGameWonWhenIsTrue() {
		gui.isGameWon(true);

		assertThat(window.label(JLabelMatcher.withText("Congratulations! YOU WON =)")).target().isShowing()).isTrue();
	}

	@Test
	public void testIsGameWonWhenIsFalse() {
		gui.isGameWon(false);

		assertThat(window.label(JLabelMatcher.withText("OH NO! You've finished your remaining attempts =(")).target().isShowing()).isTrue();
	}

	@Test
	@GUITest
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrown() {
		gui.printExceptionMessage(new CharAbsenceException("The typed char is not present, please retry.."), 'a');

		assertThat(window.textBox("missesTextBox").target().getText()).isEqualTo(" " + 'A');
		assertThat(window.label(JLabelMatcher.withText("The typed char is not present, please retry..")).target().isShowing()).isTrue();
	}

	@Test
	@GUITest
	public void testPrintExceptionMessageWhenAlreadyTypedExceptionIsThrown() {
		window.textBox("missesTextBox").setText(" " + Character.toUpperCase('e'));

		gui.printExceptionMessage(new AlreadyTypedException("Already typed char, please retry.."), 'e');

		assertThat(window.textBox("missesTextBox").target().getText()).isEqualTo(" " + 'E');
		assertThat(window.label(JLabelMatcher.withText("Already typed char, please retry..")).target().isShowing()).isTrue();
	}

	@Test
	public void testPrintExceptionMessageWhenAlphabeticCharExceptionIsThrown() {
		window.textBox("missesTextBox").setText(" " + Character.toUpperCase('e'));
		gui.printExceptionMessage(
				new AlreadyTypedException("The typed char is not alphabetic, please retry with an alphabetic one."),
				'$');

		assertThat(window.textBox("missesTextBox").target().getText()).isEqualTo(" " + 'E');
		window.label(JLabelMatcher.withText("The typed char is not alphabetic, please retry with an alphabetic one."));
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForTheFirstTimeCausesErrorCounterIncrementing() {
		gui.setErrorCounter(0);

		gui.printExceptionMessage(new CharAbsenceException("The typed char is not present, please retry.."), 'a');

		assertThat(gui.getErrorCounter()).isOne();
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForMoreThanOneTimeCausesErrorCounterIncrementing() {
		gui.setErrorCounter(1);

		gui.printExceptionMessage(new CharAbsenceException("The typed char is not present, please retry.."), 'a');

		assertThat(gui.getErrorCounter()).isEqualTo(2);
	}

	@Test
	@GUITest
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForTheFirstTimeChangeImage() {
		gui.setErrorCounter(0);

		gui.printExceptionMessage(new CharAbsenceException("The typed char is not present, please retry.."), 'a');

		assertThat(window.label("image").target().getIcon().toString())
				.isEqualTo(new ImageIcon(GraphicalUI.class.getResource("/images/error_1.png")).toString());
	}

	@Test
	@GUITest
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForForMoreThanOneTimeChangeImage() {
		gui.setErrorCounter(1);
		gui.printExceptionMessage(new CharAbsenceException("The typed char is not present, please retry.."), 'a');

		assertThat(window.label("image").target().getIcon().toString())
				.isEqualTo(new ImageIcon(GraphicalUI.class.getResource("/images/error_2.png")).toString());
	}

	@Test
	@GUITest
	public void testPrintGuessingWordWhenNoInputCharIsCorrect() {
		char[] guessingWord = new char[] { '_', '_', '_', '_' };
		gui.printGuessingWord(guessingWord);

		for (int i = 0; i < GUESSING_WORD_LENGTH; i++) {
			assertThat(window.label(JLabelMatcher.withName("finalWordChar" + i)).target().getText()).isEqualTo(" ");
		}
	}

	@Test
	@GUITest
	public void testPrintGuessingWordWhenAnInputCharIsCorrect() {
		char[] guessingWord = new char[] { '_', 'e', '_', '_' };
		gui.printGuessingWord(guessingWord);

		window.label(JLabelMatcher.withName("finalWordChar" + 1)).requireText("e".toUpperCase());

		for (int i = 0; i < GUESSING_WORD_LENGTH && i != 1; i++) {
			assertThat(window.label(JLabelMatcher.withName("finalWordChar" + i)).target().getText()).isEqualTo(" ");
		}
	}

	@Test
	@GUITest
	public void testPrintGuessingWordWhenFinalWordIsCompleted() {
		char[] guessingWord = new char[] { 't', 'e', 's', 't' };
		gui.printGuessingWord(guessingWord);

		for (int i = 0; i < GUESSING_WORD_LENGTH; i++) {
			assertThat(window.label(JLabelMatcher.withName("finalWordChar" + i)).target().getText()).isEqualTo(("" + guessingWord[i]).toUpperCase());
		}
	}

	@Test
	@GUITest
	public void testClickButtonTryClearsErrorMessageWhenCharIsCorrect() {
		GuiActionRunner.execute(() -> window.label("errorMessage").target().setText("Error"));
		window.textBox("charTextBox").enterText("e");
		window.button(JButtonMatcher.withText("TRY")).click();

		assertThat(window.label("errorMessage").target().getText()).isEqualTo(" ");
	}

	@Test
	@GUITest
	public void testButtonTryClearInsertLabelWhenClicked() {
		window.textBox("charTextBox").enterText("e");
		window.button(JButtonMatcher.withText("TRY")).click();

		assertThat(window.textBox("charTextBox").target().getText()).isEqualTo("");
		assertThat(window.button(JButtonMatcher.withText("TRY")).target().isEnabled()).isFalse();
	}

	@Test
	public void testClickButtonTryInsertsCharInTheQueueWhenCharInPresent() {
		window.textBox("charTextBox").enterText("e");
		window.button(JButtonMatcher.withText("TRY")).click();

		assertThat(gui.getQueue()).contains('e');
	}

	@Test
	public void testClickButtonTryLeaveQueueUnchangedWhenCharIsNotInserted() {
		gui.getQueue().clear();
		window.button(JButtonMatcher.withText("TRY")).click();

		assertThat(gui.getQueue()).isEmpty();
	}

}
