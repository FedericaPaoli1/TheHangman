package engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.atIndex;

import org.junit.Before;
import org.junit.Test;

import engine.InputController;
import engine.MainExecutive;
import engine.StringManager;
import exceptions.CharAbsenceException;
import exceptions.IllegalCharException;

public class ITMainExecutive {
	
	private StringManager manager;
	private InputController controller;
	private MainExecutive exec;

	@Before
	public void setup() {
		manager = new StringManager("test");
		controller = new InputController("test");
		exec = new MainExecutive("test", manager, controller);
	}
	
	@Test
	public void testPerformCharControl() throws Exception {
		char[] guessingWord = exec.performCharControl('t');

		assertThat(guessingWord).contains('t', atIndex(0));
		assertThat(guessingWord).contains('t', atIndex(3));
		assertThat(guessingWord).containsSubsequence('_', '_');
	}
	
	// FORSE LE ECCEZIONI NO 
	
	@Test
	public void testPerformCharControlThrowsIllegalCharException() throws Exception {
		assertThatThrownBy(() -> exec.performCharControl('$')).isInstanceOf(IllegalCharException.class);
		assertThatThrownBy(() -> exec.performCharControl('1')).isInstanceOf(IllegalCharException.class);
	}
	
	@Test
	public void testPerformCharControlThrowsCharAbsenceException() {
		assertThatThrownBy(() -> exec.performCharControl('a')).isInstanceOf(CharAbsenceException.class);
	}
	

}
