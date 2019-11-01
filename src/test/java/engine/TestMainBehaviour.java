package engine;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import exceptions.*;
import ui.UserInterface;

public class TestMainBehaviour {

	private static final String FINAL_WORD = "test";
	
	@Mock
	private UserInterface ui;
	
	@Spy
	private MainExecutive executive = new MainExecutive(
			new StringManager(FINAL_WORD), 
			new InputController(FINAL_WORD)
		);
	
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
		assertThat(behaviour.getErrorCounter()).isOne();
		verify(ui).printExceptionMessage(isA(CharAbsenceException.class), eq('a'));
	}

	@Test
	public void testExecuteControlWhenInputCharIsAlreadyTyped() throws Exception {

		doThrow(new AlreadyTypedException("Char already typed")).when(executive).performCharControl(anyChar());

		behaviour.executeControl(anyChar());

		verify(executive).performCharControl(anyChar());
		verify(ui).printExceptionMessage(isA(AlreadyTypedException.class), anyChar());
	}

	@Test
	public void testExecuteControlWhenInputCharIsNotAlphabetic() throws Exception {

		doThrow(new NotAlphabeticCharException("Char is not alphabetic")).when(executive).performCharControl(anyChar());

		behaviour.executeControl(anyChar());

		verify(executive).performCharControl(anyChar());
		verify(ui).printExceptionMessage(isA(IllegalCharException.class), anyChar());
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

		behaviour.setErrorCounter(MainBehaviour.MAX_ERROR_NUMBER + 1);

		assertThat(behaviour.isGameEnded()).isTrue();
	}

	@Test
	public void testIsGameEndedWhenErrorCounterIsEqualToMaximumNumber() {

		behaviour.setErrorCounter(MainBehaviour.MAX_ERROR_NUMBER);

		assertThat(behaviour.isGameEnded()).isTrue();
	}

	@Test
	public void testIsGameEndedWhenErrorCounterIsLessThanMaximumNumber() {

		behaviour.setErrorCounter(0);

		assertThat(behaviour.isGameEnded()).isFalse();
		
		behaviour.setErrorCounter(MainBehaviour.MAX_ERROR_NUMBER - 1);
	
		assertThat(behaviour.isGameEnded()).isFalse();
		
	}
	
	@Test
	public void testGameLoopWhenThereAreAllCorrectInputChars() {

		when(ui.getInputChar()).thenReturn('t').thenReturn('e').thenReturn('s');
		doReturn(false).doReturn(false).doReturn(false).doReturn(true).when(executive).isWordCompleted();

		behaviour.setErrorCounter(0);
		behaviour.gameLoop();
				
		verify(ui, times(3)).getInputChar();
		verify(ui, times(3)).printGuessingWord(any(char[].class));
		
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
		assertThat(behaviour.getErrorCounter()).isEqualTo(MainBehaviour.MAX_ERROR_NUMBER);

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

		behaviour.setErrorCounter(MainBehaviour.MAX_ERROR_NUMBER);
		behaviour.gameLoop();

		verify(ui).isGameWon(false);

	}
}
