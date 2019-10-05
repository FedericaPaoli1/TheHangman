package engine;

import java.util.Arrays;

public class StringManager {

	static final char EMPTY_CHAR = '_';
	private char[] guessingWord = {};
	
	public StringManager(String string) {
		this.guessingWord = new char[string.length()];
		Arrays.fill(this.guessingWord, EMPTY_CHAR);
	}

	char[] getGuessingWord() {
		return this.guessingWord;
	}
	
	void setGuessingWord(char[] guessingWord) {
		this.guessingWord = guessingWord;
	}

	public void updateGuessedWord(char c) {
		if (!Character.isAlphabetic(c))
			throw new IllegalArgumentException("Invalid character");
		
	}

}
