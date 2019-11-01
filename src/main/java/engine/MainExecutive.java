package engine;

import exceptions.AlreadyTypedException;
import exceptions.CharAbsenceException;
import exceptions.IllegalCharException;
import exceptions.NotAlphabeticCharException;

public class MainExecutive {

	private StringManager manager;
	private InputController controller;

	public MainExecutive(StringManager manager, InputController controller) {
		this.manager = manager;
		this.controller = controller;
	}

	public char[] performCharControl(char c) throws CharAbsenceException, AlreadyTypedException, IllegalCharException {
		try {
			if (controller.isAlreadyTyped(c))
				throw new AlreadyTypedException("Already typed char, please retry..");
		} catch (NotAlphabeticCharException notAlph) {
			throw notAlph;
		}
		if (!controller.isPresent(c))
			throw new CharAbsenceException("The typed char is not present, please retry..");
		manager.updateGuessedWord(c);
		return manager.getGuessingWord();
	}

	public boolean isWordCompleted() {
		return manager.isWordCompleted();
	}

}
