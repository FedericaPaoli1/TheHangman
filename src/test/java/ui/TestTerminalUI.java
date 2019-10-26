package ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import exceptions.CharAbsenceException;
import exceptions.IllegalCharException;
import graphics.Stickman;

public class TestTerminalUI {

	private InputStream in;
	private Scanner scanner;
	private TerminalUI terminal;

	@Before
	public void setup() {
		in = mock(InputStream.class);
		scanner = new Scanner(in);
		terminal = new TerminalUI(scanner, 4);
	}
	
	@Test
	public void testGuessingWordIsPrintedWhenGameStarts() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);
		
		terminal = new TerminalUI(scanner, 4);

		verify(out).println("\nGuessing word:");
		verify(out).println("_ _ _ _");
	}

	@Test
	public void testGuessingWordOfAnotherLengthIsPrintedWhenGameStarts() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal = new TerminalUI(scanner, 6);

		verify(out).println("\nGuessing word:");
		verify(out).println("_ _ _ _ _ _");
	}

	@Test
	public void testErrorCounterIsZeroWhenGameStarts() {
		assertThat(terminal.getErrorCounter()).isZero();
	}

	@Test
	public void testGetInputCharWhenCharIsRequested() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);
		scanner = new Scanner(new ByteArrayInputStream("e".getBytes()));

		terminal = new TerminalUI(scanner, 4);

		char c = terminal.getInputChar();

		verify(out).print("Insert a char: ");
	}

	@Test
	public void testGetInputCharWhenCharIsTyped() throws IOException {
		scanner = new Scanner(new ByteArrayInputStream("e".getBytes()));

		terminal = new TerminalUI(scanner, 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('e');
	}

	@Test
	public void testGetInputCharWhenAnotherCharIsTyped() throws IOException {
		scanner = new Scanner(new ByteArrayInputStream("a".getBytes()));

		terminal = new TerminalUI(scanner, 4);
		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('a');
	}

	@Test
	public void testGetInputCharWhenSeveralCharsAreTyped() throws IOException {
		scanner = new Scanner(new ByteArrayInputStream("abc".getBytes()));

		terminal = new TerminalUI(scanner, 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('a');
	}

	@Test
	public void testGetInputCharWhenNumberCharIsTyped() throws IOException {
		scanner = new Scanner(new ByteArrayInputStream("1".getBytes()));

		terminal = new TerminalUI(scanner, 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('1');
	}

	@Test
	public void testGetInputCharWhenSpecialCharIsTyped() throws IOException {
		scanner = new Scanner(new ByteArrayInputStream("$".getBytes()));

		terminal = new TerminalUI(scanner, 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('$');
	}

	@Test
	public void testGetInputCharWhenWhiteSpaceCharIsTyped() throws IOException {
		scanner = new Scanner(new ByteArrayInputStream(" ".getBytes()));

		terminal = new TerminalUI(scanner, 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo(' ');
	}
	
	@Test
	public void testGetInputCharWhenNoCharIsTyped() throws IOException {
		scanner = new Scanner(new ByteArrayInputStream("".getBytes()));

		terminal = new TerminalUI(scanner, 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo(' ');
	}

	@Test
	public void testIsGameWonWhenFinalWordIsCompleted() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.isGameWon(true);

		verify(out).println("Congratulations!\nYOU WON =)\n--------GAME OVER--------");
	}

	@Test
	public void testIsGameWonWhenFinalWordIsNotCompleted() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.isGameWon(false);

		verify(out).println("OH NO!\nYou've finished your remaining attempts =(\n--------GAME OVER--------");
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrown() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.printExceptionMessage(new CharAbsenceException("Char not present"), 'a');

		assertThat(terminal.getMisses()).containsOnlyOnce('a');
		verify(out).println("Char not present");
	}

	@Test
	public void testPrintExceptionMessageWhenAlreadyTypedExceptionIsThrown() {
		terminal.setMisses(Arrays.asList('e'));
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.printExceptionMessage(new IllegalCharException("Char already typed"), 'e');

		assertThat(terminal.getMisses()).containsOnlyOnce('e');
		verify(out).println("Char already typed");
	}

	@Test
	public void testPrintExceptionMessageWhenNotAlphabeticCharExceptionIsThrown() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.printExceptionMessage(new IllegalCharException("Char is not alphabetic"), '$');

		assertThat(terminal.getMisses()).doesNotContain('$');
		verify(out).println("Char is not alphabetic");
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForTheFirstTimeCausesErrorCounterIncrementing() {
		terminal.setErrorCounter(0);

		terminal.printExceptionMessage(new CharAbsenceException("Char not present"), 'a');

		assertThat(terminal.getErrorCounter()).isOne();
	}

	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForMoreThanOneTimeCausesErrorCounterIncrementing() {
		terminal.setErrorCounter(1);

		terminal.printExceptionMessage(new CharAbsenceException("Char not present"), 'a');

		assertThat(terminal.getErrorCounter()).isEqualTo(2);
	}
	
	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForTheFirstTimeCausesStickmanPrinting() {
		PrintStream out = mock(PrintStream.class);
		terminal.setErrorCounter(0);

		System.setOut(out);

		terminal.printExceptionMessage(new CharAbsenceException("Char not present"), 'a');

		InOrder inOrder = inOrder(out);
		inOrder.verify(out).println("Char not present");
		inOrder.verify(out)
				.println(Arrays.toString(Stickman.FIGURES[1]).replace("[", "").replace("]", "").replace(", ", "\n"));
	}
	
	@Test
	public void testPrintExceptionMessageWhenCharAbsenceExceptionIsThrownForForMoreThanOneTimeCausesStickmanPrinting() {
		PrintStream out = mock(PrintStream.class);
		terminal.setErrorCounter(1);

		System.setOut(out);

		terminal.printExceptionMessage(new CharAbsenceException("Char not present"), 'a');

		InOrder inOrder = inOrder(out);
		inOrder.verify(out).println("Char not present");
		inOrder.verify(out)
				.println(Arrays.toString(Stickman.FIGURES[2]).replace("[", "").replace("]", "").replace(", ", "\n"));
	}

	@Test
	public void testPrintGuessingWordWhenFinalWordIsCompleted() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.printGuessingWord(new char[] { 't', 'e', 's', 't' });

		verify(out).println("t e s t");
	}

	@Test
	public void testPrintGuessingWordWhenAnInputCharIsCorrect() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.printGuessingWord(new char[] { '_', 'e', '_', '_' });

		verify(out).println("_ e _ _");
	}

	@Test
	public void testPrintGuessingWordWhenNoInputCharIsCorrect() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.printGuessingWord(new char[] { '_', '_', '_', '_' });

		verify(out).println("_ _ _ _");
	}

	@Test
	public void testPrintStatusWhenStickmanDrawingIsPrinted() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.printStatus(0);

		verify(out).println(Arrays.toString(Stickman.FIGURES[0]).replace("[", "").replace("]", "").replace(", ", "\n"));
	}
}
