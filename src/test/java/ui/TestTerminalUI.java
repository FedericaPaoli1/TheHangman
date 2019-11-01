package ui;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exceptions.CharAbsenceException;
import exceptions.IllegalCharException;
import graphics.Stickman;

public class TestTerminalUI {

	private static final String ILLGEAL_CHAR_MESSAGE = "The typed char is not alphabetic, please retry with an alphabetic one.";
	private static final String ALREADY_TYPED_MESSAGE = "Already typed char, please retry..";
	private static final String CHAR_NOT_PRESENT_MESSAGE = "The typed char is not present, please retry..";
	private static final int FINAL_WORD_LENGTH = 4;
	private static final PrintStream OLD_OUT = System.out;

	private OutputStream out;
	private TerminalUI terminal;

	@Before
	public void setup() {
		out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		terminal = new TerminalUI(new Scanner(""), FINAL_WORD_LENGTH);
	}

	private TerminalUI settingInput(String inputChar, int finalWordLength) {
		terminal = new TerminalUI(new Scanner(inputChar), finalWordLength);
		return terminal;
	}

	@After
	public void teardown() {
		System.setOut(OLD_OUT);
	}

	@Test
	public void testGuessingWordIsPrintedWhenGameStarts() {
		terminal = settingInput("e", FINAL_WORD_LENGTH);

		assertThat(out.toString()).contains("\nGuessing word:");
		assertThat(out.toString()).contains("_ _ _ _");
	}

	@Test
	public void testGuessingWordOfAnotherLengthIsPrintedWhenGameStarts() {
		terminal = settingInput("e", 6);

		assertThat(out.toString()).contains("\nGuessing word:");
		assertThat(out.toString()).contains("_ _ _ _ _ _");
	}
	
	@Test
	public void testStickmanIsShownWhenGameStarts() {
		assertThat(out.toString()).contains(Arrays.
				toString(Stickman.IMMUTABLE_FIGURES.get(0))
				.replace("[", "")
				.replace("]", "")
				.replace(", ", "\n"));
	}
	
	@Test
	public void testErrorCounterIsZeroWhenGameStarts() {
		assertThat(terminal.getErrorCounter()).isZero();
	}

	@Test
	public void testGetInputCharWhenCharIsRequested() {

		terminal.getInputChar();

		assertThat(out.toString()).contains("Insert a char: ");
	}

	@Test
	public void testGetInputCharWhenCharIsTyped() throws IOException {
		terminal = settingInput("e", FINAL_WORD_LENGTH);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('e');
	}

	@Test
	public void testGetInputCharWhenAnotherCharIsTyped() throws IOException {
		terminal = settingInput("a", FINAL_WORD_LENGTH);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('a');
	}

	@Test
	public void testGetInputCharWhenSeveralCharsAreTyped() throws IOException {
		terminal = settingInput("abc", FINAL_WORD_LENGTH);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('a');
	}

	@Test
	public void testGetInputCharReturnLowerCaseCharWhenCharTypedIsUpperCase() {
		terminal = settingInput("A", FINAL_WORD_LENGTH);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('a');
	}

	@Test
	public void testGetInputCharWhenNumberCharIsTyped() {
		terminal = settingInput("1", FINAL_WORD_LENGTH);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('1');
	}

	@Test
	public void testGetInputCharWhenSpecialCharIsTyped() {
		terminal = settingInput("$", FINAL_WORD_LENGTH);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('$');
	}

	@Test
	public void testGetInputCharWhenWhiteSpaceCharIsTyped() {
		terminal = settingInput(" ", FINAL_WORD_LENGTH);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo(' ');
	}

	@Test
	public void testGetInputCharWhenNoCharIsTyped() {
		terminal = settingInput("", FINAL_WORD_LENGTH);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo(' ');
	}

	@Test
	public void testIsGameWonWhenFinalWordIsCompleted() {

		terminal.isGameWon(true);

		assertThat(out.toString()).contains("Congratulations!\nYOU WON =)\n--------GAME OVER--------");
	}

	@Test
	public void testIsGameWonWhenFinalWordIsNotCompleted() {

		terminal.isGameWon(false);

		assertThat(out.toString())
				.contains("OH NO!\nYou've finished your remaining attempts =(\n--------GAME OVER--------");
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrown() {
		terminal.setMisses(Lists.newArrayList());
		terminal.printExceptionMessage(new CharAbsenceException(CHAR_NOT_PRESENT_MESSAGE), 'a');

		assertThat(terminal.getMisses()).containsOnlyOnce('a');
		assertThat(out.toString()).contains(CHAR_NOT_PRESENT_MESSAGE);
	}

	@Test
	public void testPrintExceptionMessageWhenAlreadyTypedExceptionIsThrown() {
		terminal.setMisses(Arrays.asList('e'));

		terminal.printExceptionMessage(new IllegalCharException(ALREADY_TYPED_MESSAGE), 'e');

		assertThat(terminal.getMisses()).containsOnlyOnce('e');
		assertThat(out.toString()).contains(ALREADY_TYPED_MESSAGE);
	}

	@Test
	public void testPrintExceptionMessageWhenNotAlphabeticCharExceptionIsThrown() {
		terminal.setMisses(Lists.newArrayList());
		terminal.printExceptionMessage(
				new IllegalCharException(ILLGEAL_CHAR_MESSAGE), '$');

		assertThat(terminal.getMisses()).doesNotContain('$');
		assertThat(out.toString()).contains(ILLGEAL_CHAR_MESSAGE);
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForTheFirstTimeCausesErrorCounterIncrementing() {
		terminal.setErrorCounter(0);

		terminal.printExceptionMessage(
				new CharAbsenceException(CHAR_NOT_PRESENT_MESSAGE), 'a');

		assertThat(terminal.getErrorCounter()).isOne();
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForMoreThanOneTimeCausesErrorCounterIncrementing() {
		terminal.setErrorCounter(1);

		terminal.printExceptionMessage(
				new CharAbsenceException(CHAR_NOT_PRESENT_MESSAGE), 'a');

		assertThat(terminal.getErrorCounter()).isEqualTo(2);
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForTheFirstTimeCausesStickmanAndMissesPrinting() {
		terminal = settingInput("a", FINAL_WORD_LENGTH);
		terminal.setErrorCounter(0);

		terminal.printExceptionMessage(new CharAbsenceException(CHAR_NOT_PRESENT_MESSAGE), 'a');

		assertThat(out.toString())
				.contains(CHAR_NOT_PRESENT_MESSAGE)
				.contains(Arrays.toString(Stickman.IMMUTABLE_FIGURES.get(1))
						.replace("[", "|").replace("]", "_").replace(", ", "\n"))
				.contains("MISSES: [A]");
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForForMoreThanOneTimeCausesStickmanAndMissesPrinting() {
		terminal.setMisses(new LinkedList<>(Arrays.asList('b')));
		terminal.setErrorCounter(1);

		terminal.printExceptionMessage(new CharAbsenceException(CHAR_NOT_PRESENT_MESSAGE), 'a');

		assertThat(out.toString())
				.contains(CHAR_NOT_PRESENT_MESSAGE)
				.contains(Arrays.toString(Stickman.IMMUTABLE_FIGURES.get(2))
						.replace("[", "|").replace("]", "_").replace(", ", "\n"))
				.contains("MISSES: [B, A]");
	}

	@Test
	public void testPrintGuessingWordWhenFinalWordIsCompleted() {
		char[] testArray = "test".toCharArray();
		
		terminal.printGuessingWord(testArray);

		assertThat(out.toString()).containsPattern("(?m).*: T E S T$");
		assertThat(terminal.getGuessingWord()).isEqualTo(testArray);
	}

	@Test
	public void testPrintGuessingWordWhenAnInputCharIsCorrect() {
		char[] testArray = "_e__".toCharArray();

		terminal.printGuessingWord(testArray);

		assertThat(out.toString()).containsPattern("(?m).*: _ E _ _$");
		assertThat(terminal.getGuessingWord()).isEqualTo(testArray);
	}

	@Test
	public void testPrintGuessingWordWhenNoInputCharIsCorrect() {
		char[] testArray = "____".toCharArray();

		terminal.printGuessingWord(testArray);

		assertThat(out.toString()).containsPattern("(?m).*: _ _ _ _$");
		assertThat(terminal.getGuessingWord()).isEqualTo(testArray);
	}
	
	@Test
	public void testPrintGuessingWordDoesNotChangeStickmanWhenIsCalled() {
		String headSymbol = "*";
		String armSymbol = "\\";
		String legSymbol = "/";

		terminal.setErrorCounter(0);
		
		terminal.printGuessingWord(("_e__".toCharArray()));
		
		assertThat(out.toString()).doesNotContain(headSymbol)
			.doesNotContain(armSymbol)
			.doesNotContain(legSymbol);
	}
	
}
