package engine;

import exceptions.AlreadyTypedException;
import exceptions.CharAbsenceException;
import exceptions.IllegalCharException;

public class MainExecutive {

	private String finalWord;
	private StringManager manager;
	private InputController controller;

	public MainExecutive(String finalWord, StringManager manager, InputController controller) {
		this.finalWord = finalWord;
		this.manager = manager;
		this.controller = controller;
	}

	public char[] performCharControl(char c) throws CharAbsenceException, AlreadyTypedException, IllegalCharException {
		try {
			if (controller.isAlreadyTyped(c))
				throw new AlreadyTypedException();
		} catch (IllegalArgumentException e) {
			throw new IllegalCharException();
		}
		if (!controller.isPresent(c))
			throw new CharAbsenceException();
		manager.updateGuessedWord(c);
		return manager.getGuessingWord();
	}

}
