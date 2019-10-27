package engine;

import java.util.Collection;
import java.util.LinkedList;

import exceptions.NotAlphabeticCharException;

public class InputController {

	private String finalWord;
	private Collection<Character> alreadyTyped;

	public InputController(String finalWord) {
		this.finalWord = finalWord;
		this.alreadyTyped = new LinkedList<Character>();
	}

	public boolean isPresent(char c) {
		return finalWord.indexOf(c) >= 0;
	}

	public boolean isAlreadyTyped(char c) throws NotAlphabeticCharException {
		if (!Character.isAlphabetic(c))
			throw new NotAlphabeticCharException(
					"The typed char is not alphabetic, please retry with an alphabetic one.");
		if (alreadyTyped.contains(c))
			return true;

		this.alreadyTyped.add(c);
		return false;
	}

	void setAlreadyTyped(Collection<Character> alreadyTyped) {
		this.alreadyTyped = alreadyTyped;
	}

}
