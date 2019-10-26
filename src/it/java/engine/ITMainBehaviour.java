package engine;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import engine.InputController;
import engine.MainBehaviour;
import engine.MainExecutive;
import engine.StringManager;
import exceptions.CharAbsenceException;
import exceptions.IllegalCharException;
import ui.UserInterface;

public class ITMainBehaviour {

	@Mock
	private UserInterface ui;

	private MainBehaviour behaviour;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		behaviour = new MainBehaviour(new MainExecutive("test", new StringManager("test"), new InputController("test")),
				ui);
	}

	@Test
	public void testExecuteControl() throws Exception {
		behaviour.executeControl('e');

		verify(ui).printGuessingWord(new char[] { '_', 'e', '_', '_' });
	}
	
	// FORSE LE ECCEZIONI NO 

	@Test
	public void testExecuteControlThrowsCharAbsentException() throws Exception {
		behaviour.setErrorCounter(0);
		behaviour.executeControl('a');

		assertThat(behaviour.getErrorCounter()).isOne();
		verify(ui).printExceptionMessage(isA(CharAbsenceException.class), eq('a'));
	}

	@Test
	public void testExecuteControlThrowsIllegalCharException() throws Exception {
		behaviour.executeControl(eq(anyChar()));

		verify(ui).printExceptionMessage(isA(IllegalCharException.class), anyChar());
	}
	
	//

	@Test
	public void testIsGameEnded() {
		assertThat(behaviour.isGameEnded()).isFalse();
	}

	@Test
	public void testGameLoop() {
		when(ui.getInputChar()).thenReturn('t').thenReturn('e').thenReturn('s');

		behaviour.setErrorCounter(0);
		behaviour.gameLoop();

		verify(ui, times(3)).getInputChar();
		verify(ui, times(3)).printGuessingWord(any(char[].class));

		assertThat(behaviour.getErrorCounter()).isZero();
	}

}
