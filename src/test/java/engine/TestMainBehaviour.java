package engine;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import javax.print.attribute.standard.MediaSize.Other;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import exceptions.AlreadyTypedException;
import exceptions.CharAbsenceException;
import exceptions.IllegalCharException;
import exceptions.NotAlphabeticCharException;
import ui.UserInterface;

/*
 * TODO togliere lo spy dalla SUT, è necessario verificare la chimata di altri metodis
 */

public class TestMainBehaviour {

	@Mock
	private UserInterface ui;
	@Spy
	private MainExecutive executive = new MainExecutive("test", new StringManager("test"), new InputController("test"));
	@Spy
	@InjectMocks
	private MainBehaviour behaviour;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExecuteControlWhenInputCharIsPresent() throws Exception {

		doReturn(new char[] { '_', 'e', '_', '_' }).when(executive).performCharControl('e');

		behaviour.executeControl('e');

		verify(executive).performCharControl('e');
		verify(ui).printGuessingWord(new char[] { '_', 'e', '_', '_' });

	}

	@Test
	public void testExecuteControlWhenInputCharIsAbsent() throws Exception {

		behaviour.setErrorCounter(0);

		doThrow(new CharAbsenceException("Char not present")).when(executive).performCharControl('a');

		behaviour.executeControl('a');

		verify(executive).performCharControl('a');
		assertThat(behaviour.getErrorCounter()).isEqualTo(1);
		verify(ui).printExceptionMessage(isA(CharAbsenceException.class));
	}

	@Test
	public void testExecuteControlWhenInputCharIsAlreadyTyped() throws Exception {

		doThrow(new AlreadyTypedException("Char already typed")).when(executive).performCharControl(anyChar());

		behaviour.executeControl(anyChar());

		verify(executive).performCharControl(anyChar());
		verify(ui).printExceptionMessage(isA(AlreadyTypedException.class));
	}

	@Test
	public void testExecuteControlWhenInputCharIsNotAlphabetic() throws Exception {

		doThrow(new NotAlphabeticCharException("Char is not alphabetic")).when(executive).performCharControl(anyChar());

		behaviour.executeControl(anyChar());

		verify(executive).performCharControl(anyChar());
		verify(ui).printExceptionMessage(isA(IllegalCharException.class));
	}

	@Test
	public void testIsGameEndedWhenGuessingWordIsCompleted() {

		doReturn(true).when(executive).isWordCompleted();

		assertThat(behaviour.isGameEnded()).isTrue();
		verify(executive).isWordCompleted();
	}

	@Test
	public void testIsGameEndedWhenGuessingWordIsNotCompletedYet() {

		doReturn(false).when(executive).isWordCompleted();

		assertThat(behaviour.isGameEnded()).isFalse();
		verify(executive).isWordCompleted();
	}

	@Test
	public void testIsGameEndedWhenErrorCounterIsGreaterThanMaximumNumber() {

		behaviour.setErrorCounter(7);

		assertThat(behaviour.isGameEnded()).isTrue();
	}

	@Test
	public void testIsGameEndedWhenErrorCounterIsEqualToMaximumNumber() {

		behaviour.setErrorCounter(6);

		assertThat(behaviour.isGameEnded()).isTrue();
	}

	@Test
	public void testIsGameEndedWhenErrorCounterIsLessThanMaximumNumber() {

		behaviour.setErrorCounter(0);

		assertThat(behaviour.isGameEnded()).isFalse();
	}

	
	// TODO da completare
	@Test
	@Ignore
	public void testGameLoopWhenThereAreAllCorrectInputChars() {

		when(ui.getInputChar()).thenReturn('t').thenReturn('e').thenReturn('s');
		doReturn(false).doReturn(false).doReturn(false).doReturn(true).when(executive).isWordCompleted();

		behaviour.setErrorCounter(0);
		behaviour.gameLoop();
		
		InOrder inOrder = inOrder(ui); 
		
//		verify(ui, times(3)).printGuessingWord(any(char[].class));
		inOrder.verify(ui).getInputChar();
		inOrder.verify(ui).printGuessingWord(
				AdditionalMatchers.aryEq(new char[]{'t', 'e', 's', 't'}));
		inOrder.verify(ui).getInputChar();
		inOrder.verify(ui).printGuessingWord(
				AdditionalMatchers.aryEq(new char[]{'t', '_', '_', 't'}));
		inOrder.verify(ui).printGuessingWord(
				AdditionalMatchers.aryEq(new char[]{'t', 'e', '_', 't'}));
		inOrder.verify(ui).getInputChar();
		assertThat(behaviour.getErrorCounter()).isZero();
	}
	

	@Test
	public void testGameLoopWhenThereAreNoCorrectInputChars() {

		when(ui.getInputChar()).thenReturn('a')
				.thenReturn('b')
				.thenReturn('c')
				.thenReturn('d')
				.thenReturn('f')
				.thenReturn('g');
		doReturn(false).when(executive).isWordCompleted();

		behaviour.setErrorCounter(0);
		behaviour.gameLoop();
		
		verify(ui, times(6)).getInputChar();
		verify(ui, never()).printGuessingWord(any(char[].class));
		assertThat(behaviour.getErrorCounter()).isEqualTo(6);

	}

	@Test
	public void testGameLoopWhenGameIsWon() {

		doReturn(true).when(executive).isWordCompleted();

		behaviour.gameLoop();

		verify(ui).isGameWon(true);
	}

	@Test
	public void testGameLoopWhenGameIsLost() {

		doReturn(false).when(executive).isWordCompleted();

		behaviour.setErrorCounter(6);
		behaviour.gameLoop();

		verify(ui).isGameWon(false);

	}

}
