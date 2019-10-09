package engine;

import java.util.Collection;
import java.util.LinkedList;

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

	public boolean isAlreadyTyped(char c) 
	{
		if (!Character.isAlphabetic(c))
			throw new IllegalArgumentException("Invalid character");
		if (alreadyTyped.contains(c))
			return true;
	
		this.alreadyTyped.add(c);
		return false;
	}

	void setAlreadyTyped(Collection<Character> alreadyTyped) {
		this.alreadyTyped = alreadyTyped;
	}
	
}
