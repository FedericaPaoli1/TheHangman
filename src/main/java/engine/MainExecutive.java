package engine;

import exceptions.AlreadyTypedException;
import exceptions.CharAbsenceException;
import exceptions.IllegalCharException;

public class MainExecutive {

	private StringManager manager;
	private InputController controller;

	public MainExecutive(StringManager manager, InputController controller) {
		this.manager = manager;
		this.controller = controller;
	}

	public char[] performCharControl(char c) throws CharAbsenceException, IllegalCharException {
		if (controller.isAlreadyTyped(c))
			throw new AlreadyTypedException("Already typed char, please retry..");

		if (!controller.isPresent(c))
			throw new CharAbsenceException("The typed char is not present, please retry..");
		manager.updateGuessedWord(c);
		return manager.getGuessingWord();
	}

	public boolean isWordCompleted() {
		return manager.isWordCompleted();
	}

}
