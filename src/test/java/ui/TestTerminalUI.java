package ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import exceptions.CharAbsenceException;
import exceptions.IllegalCharException;
import graphics.Stickman;

public class TestTerminalUI {

	private InputStream in;
	private TerminalUI terminal;

	@Before
	public void setup() {
		in = mock(InputStream.class);
		terminal = new TerminalUI(in, 4);
	}
	
	@Test
	public void testGuessingWordIsPrintedWhenGameStarts() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal = new TerminalUI(in, 4);

		verify(out).println("\nGuessing word:");
		verify(out).print("_ _ _ _");
	}

	@Test
	public void testGuessingWordOfAnotherLengthIsPrintedWhenGameStarts() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal = new TerminalUI(in, 6);

		verify(out).println("\nGuessing word:");
		verify(out).print("_ _ _ _ _ _");
	}

	@Test
	public void testErrorCounterIsZeroWhenGameStarts() {
		terminal = new TerminalUI(in, 4);

		assertThat(terminal.getErrorCounter()).isZero();
	}

	@Test
	public void testGetInputCharWhenCharIsRequested() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);
		System.setIn(new ByteArrayInputStream("e".getBytes()));

		terminal = new TerminalUI(System.in, 4);

		char c = terminal.getInputChar();

		verify(out).print("Insert a char: ");
	}

	@Test
	public void testGetInputCharWhenCharIsTyped() throws IOException {
		System.setIn(new ByteArrayInputStream("e".getBytes()));
		terminal = new TerminalUI(System.in, 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('e');
	}

	@Test
	public void testGetInputCharWhenAnotherCharIsTyped() throws IOException {
		System.setIn(new ByteArrayInputStream("a".getBytes()));
		terminal = new TerminalUI(System.in, 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('a');
	}

	@Test
	public void testGetInputCharWhenSeveralCharsAreTyped() throws IOException {
		System.setIn(new ByteArrayInputStream("abc".getBytes()));
		terminal = new TerminalUI(System.in, 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('a');
	}

	@Test
	public void testGetInputCharWhenNumberCharIsTyped() throws IOException {
		System.setIn(new ByteArrayInputStream("1".getBytes()));
		terminal = new TerminalUI(System.in, 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('1');
	}

	@Test
	public void testGetInputCharWhenSpecialCharIsTyped() throws IOException {
		System.setIn(new ByteArrayInputStream("$".getBytes()));
		terminal = new TerminalUI(System.in, 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo('$');
	}

	@Test
	public void testGetInputCharWhenWhiteSpaceCharIsTyped() throws IOException {
		System.setIn(new ByteArrayInputStream(" ".getBytes()));
		terminal = new TerminalUI(System.in, 4);

		char returned = terminal.getInputChar();

		assertThat(returned).isEqualTo(' ');
	}

	@Test
	public void testGetInputCharWhenNoCharIsTyped() throws IOException {
		System.setIn(new ByteArrayInputStream("".getBytes()));
		terminal = new TerminalUI(System.in, 4);

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

		terminal.printExceptionMessage(new CharAbsenceException("Char not present"));

		verify(out).println("Char not present");
	}

	@Test
	public void testPrintExceptionMessageWhenAlreadyTypedExceptionIsThrown() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.printExceptionMessage(new IllegalCharException("Char already typed"));

		verify(out).println("Char already typed");
	}

	@Test
	public void testPrintExceptionMessageWhenNotAlphabeticCharExceptionIsThrown() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.printExceptionMessage(new IllegalCharException("Char is not alphabetic"));

		verify(out).println("Char is not alphabetic");
	}

	@Test
	public void testPrintExceptionMessageWhenNotAlphabeticCharExceptionIsThrownForTheFirstTimeCausesErrorCounterIncrementing() {
		terminal.setErrorCounter(0);

		terminal.printExceptionMessage(new CharAbsenceException("Char not present"));

		assertThat(terminal.getErrorCounter()).isOne();
	}

	@Test
	public void testPrintExceptionMessageWhenNotAlphabeticCharExceptionIsThrownForMoreThanOneTimeCausesErrorCounterIncrementing() {
		terminal.setErrorCounter(1);

		terminal.printExceptionMessage(new CharAbsenceException("Char not present"));

		assertThat(terminal.getErrorCounter()).isEqualTo(2);
	}
	
	@Test
	public void testPrintExceptionMessageWhenNotAlphabeticCharExceptionIsThrownForTheFirstTimeCausesStickmanPrinting() {
		PrintStream out = mock(PrintStream.class);
		terminal.setErrorCounter(0);

		System.setOut(out);

		terminal.printExceptionMessage(new CharAbsenceException("Char not present"));

		InOrder inOrder = inOrder(out);
		inOrder.verify(out).println("Char not present");
		inOrder.verify(out)
				.print(Arrays.toString(Stickman.FIGURES[1]).replace("[", "").replace("]", "").replace(", ", ""));
	}
	
	@Test
	public void testPrintExceptionMessageWhenNotAlphabeticCharExceptionIsThrownForForMoreThanOneTimeCausesStickmanPrinting() {
		PrintStream out = mock(PrintStream.class);
		terminal.setErrorCounter(1);

		System.setOut(out);

		terminal.printExceptionMessage(new CharAbsenceException("Char not present"));

		InOrder inOrder = inOrder(out);
		inOrder.verify(out).println("Char not present");
		inOrder.verify(out)
				.print(Arrays.toString(Stickman.FIGURES[2]).replace("[", "").replace("]", "").replace(", ", ""));
	}

	@Test
	public void testPrintGuessingWordWhenFinalWordIsCompleted() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.printGuessingWord(new char[] { 't', 'e', 's', 't' });

		verify(out).print("t e s t");
	}

	@Test
	public void testPrintGuessingWordWhenAnInputCharIsCorrect() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.printGuessingWord(new char[] { '_', 'e', '_', '_' });

		verify(out).print("_ e _ _");
	}

	@Test
	public void testPrintGuessingWordWhenNoInputCharIsCorrect() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.printGuessingWord(new char[] { '_', '_', '_', '_' });

		verify(out).print("_ _ _ _");
	}

	@Test
	public void testPrintStatusWhenStickmanDrawingIsPrinted() {
		PrintStream out = mock(PrintStream.class);

		System.setOut(out);

		terminal.printStatus(0);

		verify(out).print(Arrays.toString(Stickman.FIGURES[0]).replace("[", "").replace("]", "").replace(", ", ""));
	}
}
