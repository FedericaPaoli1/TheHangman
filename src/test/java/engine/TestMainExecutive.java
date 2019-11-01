package engine;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import exceptions.AlreadyTypedException;
import exceptions.CharAbsenceException;
import exceptions.NotAlphabeticCharException;

public class TestMainExecutive {

	private static final String FINAL_WORD = "test";
	private MainExecutive exec;
	private StringManager manager;
	private InputController controller;

	@Before
	public void setup() {
		manager = spy(new StringManager(FINAL_WORD));
		controller = spy(new InputController(FINAL_WORD));
		exec = new MainExecutive(manager, controller);
	}

	@Test
	public void testPerformCharControlWhenCharIsPresent() throws Exception {

		doNothing().when(manager).updateGuessedWord('e');
		doReturn(new char[] { '_', 'e', '_', '_' }).when(manager).getGuessingWord();

		char[] guessingWord = exec.performCharControl('e');

		assertThat(guessingWord).contains('e', atIndex(1));
		assertThat(guessingWord).containsSubsequence('_', '_', '_');

		verify(manager).updateGuessedWord('e');
		verify(manager).getGuessingWord();
	}

	@Test
	public void testPerformCharControlWhenSeveralCharsArePresent() throws Exception {
		doNothing().when(manager).updateGuessedWord('t');
		doReturn(new char[] { 't', '_', '_', 't' }).when(manager).getGuessingWord();

		char[] guessingWord = exec.performCharControl('t');

		assertThat(guessingWord).contains('t', atIndex(0));
		assertThat(guessingWord).contains('t', atIndex(3));
		assertThat(guessingWord).containsSubsequence('_', '_');

		verify(manager).updateGuessedWord('t');
		verify(manager).getGuessingWord();
	}

	@Test
	public void testPerformCharControlWhenCharIsNotPresent() {
		doReturn(false).when(controller).isPresent('a');

		assertThatThrownBy(() -> exec.performCharControl('a')).isInstanceOf(CharAbsenceException.class);
		verify(manager, never()).updateGuessedWord(anyChar());
	}

	@Test
	public void testPerformCharControlWhenCharIsAlreadyTyped() throws Exception {
		doReturn(true).when(controller).isAlreadyTyped(anyChar());

		assertThatThrownBy(() -> exec.performCharControl(anyChar())).isInstanceOf(AlreadyTypedException.class);
		
		verify(controller, never()).isPresent(anyChar());
		verify(manager, never()).updateGuessedWord(anyChar());
	}
	
	@Test
	public void testPerformCharControlWhenCharIsNotAlphabetic() throws Exception {
		doThrow(NotAlphabeticCharException.class).when(controller).isAlreadyTyped('$');

		assertThatThrownBy(() -> exec.performCharControl('$')).isInstanceOf(NotAlphabeticCharException.class);
		
		verify(controller, never()).isPresent(anyChar());
		verify(manager, never()).updateGuessedWord(anyChar());
	}
	
	@Test
	public void testIsWordCompletedWhenNoEmptyCharAppears() {
		doReturn(true).when(manager).isWordCompleted();
		
		assertThat(exec.isWordCompleted()).isTrue();
		verify(manager).isWordCompleted();
	}
	
	@Test
	public void testIsWordCompletedWhenOneOrMoreEmptyCharAppear() {
		doReturn(false).when(manager).isWordCompleted();
		
		assertThat(exec.isWordCompleted()).isFalse();
		verify(manager).isWordCompleted();
	}
}
