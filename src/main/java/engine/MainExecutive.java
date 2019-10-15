package engine;

import exceptions.AlreadyTypedException;
import exceptions.CharAbsenceException;
import exceptions.IllegalCharException;
import exceptions.NotAlphabeticCharException;

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
				throw new AlreadyTypedException("Char already typed");
		} catch (NotAlphabeticCharException notAlph) {
			throw notAlph;
		}
		if (!controller.isPresent(c))
			throw new CharAbsenceException("Char not present");
		manager.updateGuessedWord(c);
		return manager.getGuessingWord();
	}

	public boolean isWordCompleted() {
		return manager.isWordCompleted();
	}

}
