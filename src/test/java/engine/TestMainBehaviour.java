package engine;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.PrintStream;

import org.junit.Test;
import org.mockito.InOrder;

import exceptions.AlreadyTypedException;
import exceptions.CharAbsenceException;
import exceptions.NotAlphabeticCharException;
import ui.UserInterface;

public class TestMainBehaviour {

	@Test
	public void testExecuteControlWhenInputCharIsPresent() throws Exception {
		PrintStream out = mock(PrintStream.class);
		MainExecutive executive = spy(new MainExecutive("test", new StringManager("test"), new InputController("test")));
		MainBehaviour behaviour = new MainBehaviour(executive);
		
		doReturn(new char[] {'_', 'e','_','_'}).when(executive).performCharControl('e');
		System.setOut(out);
		
		behaviour.executeControl('e');
		
		verify(executive).performCharControl('e');
		verify(out).println("[_, e, _, _]");
		
	}
	
	@Test
	public void testExecuteControlWhenInputCharIsAbsent() throws Exception {
		PrintStream out = mock(PrintStream.class);
		MainExecutive executive = spy(new MainExecutive("test", new StringManager("test"), new InputController("test")));
		MainBehaviour behaviour = new MainBehaviour(executive);
		behaviour.setErrorCounter(0);
		
		System.setOut(out);
		
		doThrow(new CharAbsenceException("Char not present")).when(executive).performCharControl('a');
		
		behaviour.executeControl('a');
		
		verify(executive).performCharControl('a');
		assertThat(behaviour.getErrorCounter()).isEqualTo(1);
		verify(out).println("Char not present");
	}
	
	@Test
	public void testExecuteControlWhenInputCharIsAlreadyTyped() throws Exception {
		PrintStream out = mock(PrintStream.class);
		MainExecutive executive = spy(new MainExecutive("test", new StringManager("test"), new InputController("test")));
		MainBehaviour behaviour = new MainBehaviour(executive);
		
		doThrow(new AlreadyTypedException("Char already typed")).when(executive).performCharControl(anyChar());
		System.setOut(out);
		
		behaviour.executeControl(anyChar());
		
		verify(executive).performCharControl(anyChar());
		verify(out).println("Char already typed");
	}
	

	@Test
	public void testExecuteControlWhenInputCharIsNotAlphabetic() throws Exception {
		PrintStream out = mock(PrintStream.class);
		MainExecutive executive = spy(new MainExecutive("test", new StringManager("test"), new InputController("test")));
		MainBehaviour behaviour = new MainBehaviour(executive);
		
		doThrow(new NotAlphabeticCharException("Char is not alphabetic")).when(executive).performCharControl(anyChar());
		System.setOut(out);
		
		behaviour.executeControl(anyChar());
		
		verify(executive).performCharControl(anyChar());
		verify(out).println("Char is not alphabetic");
	}
	
	@Test
	public void testIsGameEndedWhenGuessingWordIsCompleted() {
		MainExecutive executive = spy(new MainExecutive("test", new StringManager("test"), new InputController("test")));
		MainBehaviour behaviour = spy(new MainBehaviour(executive));

		doReturn(true).when(executive).isWordCompleted();
		
		assertThat(behaviour.isGameEnded()).isTrue();
		verify(executive).isWordCompleted();
	}
	
	@Test
	public void testIsGameEndedWhenGuessingWordIsNotCompletedYet() {
		MainExecutive executive = spy(new MainExecutive("test", new StringManager("test"), new InputController("test")));
		MainBehaviour behaviour = spy(new MainBehaviour(executive));

		doReturn(false).when(executive).isWordCompleted();
		
		assertThat(behaviour.isGameEnded()).isFalse();
		verify(executive).isWordCompleted();
	}
	
	@Test
	public void testIsGameEndedWhenErrorCounterIsGreaterThanMaximumNumber() {
		MainExecutive executive = spy(new MainExecutive("test", new StringManager("test"), new InputController("test")));
		MainBehaviour behaviour = spy(new MainBehaviour(executive));

		behaviour.setErrorCounter(7);
		
		assertThat(behaviour.isGameEnded()).isTrue();
	}
	
	@Test
	public void testIsGameEndedWhenErrorCounterIsEqualToMaximumNumber() {
		MainExecutive executive = spy(new MainExecutive("test", new StringManager("test"), new InputController("test")));
		MainBehaviour behaviour = spy(new MainBehaviour(executive));

		behaviour.setErrorCounter(6);
		
		assertThat(behaviour.isGameEnded()).isTrue();
	}
	
	@Test
	public void testIsGameEndedWhenErrorCounterIsLessThanMaximumNumber() {
		MainExecutive executive = spy(new MainExecutive("test", new StringManager("test"), new InputController("test")));
		MainBehaviour behaviour = spy(new MainBehaviour(executive));

		behaviour.setErrorCounter(0);
		
		assertThat(behaviour.isGameEnded()).isFalse();
	}
	
	public void testGameLoopWhenThereAreAllCorrectInputChars() {
		UserInterface ui = mock(UserInterface.class);
		MainExecutive executive = spy(new MainExecutive("test", new StringManager("test"), new InputController("test")));
		MainBehaviour behaviour = spy(new MainBehaviour(executive, ui));

		when(ui.getInputChar()).thenReturn('t').thenReturn('e').thenReturn('s');
		doReturn(false).doReturn(false).doReturn(false).doReturn(true).when(executive).isWordCompleted();
		
		behaviour.setErrorCounter(0);
		behaviour.gameLoop();
		
		InOrder inOrder = inOrder(behaviour, ui);
		inOrder.verify(ui).getInputChar();
		inOrder.verify(behaviour).executeControl('t');
		inOrder.verify(ui).getInputChar();
		inOrder.verify(behaviour).executeControl('e');
		inOrder.verify(ui).getInputChar();
		inOrder.verify(behaviour).executeControl('s');
	}
	
	@Test
	public void testGameLoopWhenThereAreNoCorrectInputChars() {
		UserInterface ui = mock(UserInterface.class);
		MainExecutive executive = spy(new MainExecutive("test", new StringManager("test"), new InputController("test")));
		MainBehaviour behaviour = spy(new MainBehaviour(executive, ui));

		when(ui.getInputChar()).thenReturn('a').thenReturn('b').thenReturn('c').thenReturn('d').thenReturn('f').thenReturn('g');
		doReturn(false).when(executive).isWordCompleted();
		
		behaviour.setErrorCounter(0);
		behaviour.gameLoop();
		
		InOrder inOrder = inOrder(behaviour, ui);
		inOrder.verify(ui).getInputChar();
		inOrder.verify(behaviour).executeControl('a');
		inOrder.verify(ui).getInputChar();
		inOrder.verify(behaviour).executeControl('b');
		inOrder.verify(ui).getInputChar();
		inOrder.verify(behaviour).executeControl('c');
		inOrder.verify(ui).getInputChar();
		inOrder.verify(behaviour).executeControl('d');
		inOrder.verify(ui).getInputChar();
		inOrder.verify(behaviour).executeControl('f');
		inOrder.verify(ui).getInputChar();
		inOrder.verify(behaviour).executeControl('g');
	}
	
	@Test
	public void testGameLoopWhenGameIsWon() {
		UserInterface ui = mock(UserInterface.class);
		MainExecutive executive = spy(new MainExecutive("test", new StringManager("test"), new InputController("test")));
		MainBehaviour behaviour = spy(new MainBehaviour(executive, ui));
		
		doReturn(true).when(executive).isWordCompleted();
		
		behaviour.gameLoop();
		
		verify(ui).isGameWon(true);
	}
	
	@Test
	public void testGameLoopWhenGameIsLost() {
		UserInterface ui = mock(UserInterface.class);
		MainExecutive executive = spy(new MainExecutive("test", new StringManager("test"), new InputController("test")));
		MainBehaviour behaviour = spy(new MainBehaviour(executive, ui));
		
		doReturn(false).when(executive).isWordCompleted();
		
		behaviour.setErrorCounter(6);
		behaviour.gameLoop();
		
		verify(ui).isGameWon(false);
		
	}
	

}
