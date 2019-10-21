package engine;

import java.util.Arrays;

public class StringManager {

	static final char EMPTY_CHAR = '_';
	
	private char[] guessingWord = {};
	private String finalWord;
	
	public StringManager(String finalWord) 
	{
		this.finalWord = finalWord;
		this.guessingWord = new char[finalWord.length()];
		Arrays.fill(this.guessingWord, EMPTY_CHAR);
	}

	char[] getGuessingWord() {
		return this.guessingWord;
	}
	
	void setGuessingWord(char[] guessingWord) {
		this.guessingWord = guessingWord;
	}

	public void updateGuessedWord(char c) {
		for (int index = finalWord.indexOf(c); index >= 0; index = finalWord.indexOf(c, index + 1))
			guessingWord[index] = c;
	
	}
	
	
	public boolean isWordCompleted() {
		for(char c: guessingWord) {
			if(c == '_')
				return false;
		}
		return true;
	}

}
