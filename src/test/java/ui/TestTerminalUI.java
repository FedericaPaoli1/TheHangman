package ui;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exceptions.CharAbsenceException;
import exceptions.IllegalCharException;
import graphics.Stickman;

public class TestTerminalUI {

	private InputStream in;
	private OutputStream out;
	private Scanner scanner;
	private TerminalUI terminal;

	@Before
	public void setup() {
		out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
	}

	private TerminalUI settingInputStream(String inputChar, int finalWordLength) {
		in = new ByteArrayInputStream(inputChar.getBytes());
		System.setIn(in);
		scanner = new Scanner(in);
		terminal = new TerminalUI(new Scanner(in), finalWordLength);
		return terminal;
	}

	@After
	public void teardown() {
		System.setOut(null);
	}

	@Test
	public void testGuessingWordIsPrintedWhenGameStarts() {
		terminal = settingInputStream("e", 4);

		assertThat(out.toString()).contains("\nGuessing word:");
		assertThat(out.toString()).contains("_ _ _ _");
	}

	@Test
	public void testGuessingWordOfAnotherLengthIsPrintedWhenGameStarts() {
		terminal = settingInputStream("e", 6);

		assertThat(out.toString()).contains("\nGuessing word:");
		assertThat(out.toString()).contains("_ _ _ _ _ _");
	}

	@Test
	public void testErrorCounterIsZeroWhenGameStarts() {
		terminal = settingInputStream("e", 4);
		assertThat(terminal.getErrorCounter()).isZero();
	}

	@Test
	public void testGetInputCharWhenCharIsRequested() {
		terminal = settingInputStream("e", 4);

		char c = terminal.getInputChar();

		assertThat(out.toString()).contains("Insert a char: ");
	}

	@Test
	public void testGetInputCharWhenCharIsTyped() throws IOException {
		terminal = settingInputStream("e", 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('e');
	}

	@Test
	public void testGetInputCharWhenAnotherCharIsTyped() throws IOException {
		terminal = settingInputStream("a", 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('a');
	}

	@Test
	public void testGetInputCharWhenSeveralCharsAreTyped() throws IOException {
		terminal = settingInputStream("a", 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('a');
	}

	@Test
	public void testGetInputCharWhenNumberCharIsTyped() throws IOException {
		terminal = settingInputStream("1", 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('1');
	}

	@Test
	public void testGetInputCharWhenSpecialCharIsTyped() throws IOException {
		terminal = settingInputStream("$", 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('$');
	}

	@Test
	public void testGetInputCharWhenWhiteSpaceCharIsTyped() throws IOException {
		terminal = settingInputStream(" ", 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo(' ');
	}

	@Test
	public void testGetInputCharWhenNoCharIsTyped() throws IOException {
		terminal = settingInputStream("", 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo(' ');
	}

	@Test
	public void testIsGameWonWhenFinalWordIsCompleted() {
		String[] inputs = {"t", "e", "s"};
		for (int i = 0; i < inputs.length; i++) {
			terminal = settingInputStream(inputs[i], 4);
		}

		terminal.isGameWon(true);

		assertThat(out.toString()).contains("Congratulations!\nYOU WON =)\n--------GAME OVER--------");
	}

	@Test
	public void testIsGameWonWhenFinalWordIsNotCompleted() {
		String[] inputs = {"e", "s"};
		for (int i = 0; i < inputs.length; i++) {
			terminal = settingInputStream(inputs[i], 4);
		}


		terminal.isGameWon(false);

		assertThat(out.toString())
				.contains("OH NO!\nYou've finished your remaining attempts =(\n--------GAME OVER--------");
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrown() {
		terminal = settingInputStream("a", 4);

		terminal.printExceptionMessage(new CharAbsenceException("The typed char is not present, please retry.."), 'a');

		assertThat(terminal.getMisses()).containsOnlyOnce('a');
		assertThat(out.toString()).contains("The typed char is not present, please retry..");
	}

	@Test
	public void testPrintExceptionMessageWhenAlreadyTypedExceptionIsThrown() {
		terminal = settingInputStream("e", 4);
		terminal = settingInputStream("e", 4);
		terminal.setMisses(Arrays.asList('e'));

		terminal.printExceptionMessage(new IllegalCharException("Already typed char, please retry.."), 'e');

		assertThat(terminal.getMisses()).containsOnlyOnce('e');
		assertThat(out.toString()).contains("Already typed char, please retry..");
	}

	@Test
	public void testPrintExceptionMessageWhenNotAlphabeticCharExceptionIsThrown() { //
		terminal = settingInputStream("$", 4);

		terminal.printExceptionMessage(new IllegalCharException("The typed char is not alphabetic, please retry with an alphabetic one."), '$');

		assertThat(terminal.getMisses()).doesNotContain('$');
		assertThat(out.toString()).contains("The typed char is not alphabetic, please retry with an alphabetic one.");
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForTheFirstTimeCausesErrorCounterIncrementing() {
		terminal = settingInputStream("a", 4);
		terminal.setErrorCounter(0);

		terminal.printExceptionMessage(new CharAbsenceException("The typed char is not present, please retry.."), 'a');

		assertThat(terminal.getErrorCounter()).isOne();
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForMoreThanOneTimeCausesErrorCounterIncrementing() {
		terminal = settingInputStream("a", 4);
		terminal = settingInputStream("b", 4);
		terminal.setErrorCounter(1);

		terminal.printExceptionMessage(new CharAbsenceException("The typed char is not present, please retry.."), 'a');

		assertThat(terminal.getErrorCounter()).isEqualTo(2);
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForTheFirstTimeCausesStickmanAndMissesPrinting() {
		terminal = settingInputStream("a", 4);
		terminal.setErrorCounter(0);

		terminal.printExceptionMessage(new CharAbsenceException("The typed char is not present, please retry.."), 'a');

//		InOrder inOrder = inOrder(out);
		assertThat(out.toString()).contains("The typed char is not present, please retry..");
		assertThat(out.toString())
				.contains(Arrays.toString(Stickman.FIGURES[1]).replace("[", "").replace("]", "").replace(", ", "\n"));
		assertThat(out.toString()).contains("MISSES: ");
		assertThat(out.toString()).contains("[A]");
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForForMoreThanOneTimeCausesStickmanAndMissesPrinting() {
		terminal = settingInputStream("a", 4);
		terminal = settingInputStream("b", 4);
		terminal.setErrorCounter(1);

		terminal.printExceptionMessage(new CharAbsenceException("The typed char is not present, please retry.."), 'a');

//		InOrder inOrder = inOrder(out);
		assertThat(out.toString()).contains("The typed char is not present, please retry..");
		assertThat(out.toString())
				.contains(Arrays.toString(Stickman.FIGURES[2]).replace("[", "").replace("]", "").replace(", ", "\n"));
		assertThat(out.toString()).contains("MISSES: ");
		assertThat(out.toString()).contains("[A]");
	}

	@Test
	public void testPrintGuessingWordWhenFinalWordIsCompleted() {
		String[] inputs = {"t", "e", "s"};
		for (int i = 0; i < inputs.length; i++) {
			terminal = settingInputStream(inputs[i], 4);
		}		

		terminal.printGuessingWord(new char[] { 't', 'e', 's', 't' });

		assertThat(out.toString()).contains("T E S T");
	}

	@Test
	public void testPrintGuessingWordWhenAnInputCharIsCorrect() { 
		terminal = settingInputStream("e", 4);
		
		terminal.printGuessingWord(new char[] { '_', 'e', '_', '_' });

		assertThat(out.toString()).contains("_ E _ _");
	}

	@Test
	public void testPrintGuessingWordWhenNoInputCharIsCorrect() { 
		String[] inputs = {"a", "b", "c", "d", "f", "g"};
		for (int i = 0; i < inputs.length; i++) {
			terminal = settingInputStream(inputs[i], 4);
		}
		
		terminal.printGuessingWord(new char[] { '_', '_', '_', '_' });

		assertThat(out.toString()).contains("_ _ _ _");
	}

	@Test
	public void testPrintStatusWhenStickmanDrawingIsPrinted() { 
		terminal = settingInputStream("", 4);

		terminal.printStatus(0);

		assertThat(out.toString()).contains(Arrays.toString(Stickman.FIGURES[0]).replace("[", "").replace("]", "").replace(", ", "\n"));
	}

}
