package engine;

import java.util.Arrays;
import java.util.List;

public class StringManager {

	static final char EMPTY_CHAR = '_';
	private char[] guessingWord = {};
	StringAnalyser analyser;
	
	public StringManager(String finalWord) {
		this.guessingWord = new char[finalWord.length()];
		this.analyser =  new StringAnalyser(finalWord);
		Arrays.fill(this.guessingWord, EMPTY_CHAR);
	}

	char[] getGuessingWord() {
		return this.guessingWord;
	}
	
	void setGuessingWord(char[] guessingWord) {
		this.guessingWord = guessingWord;
	}

	public void updateGuessedWord(char c) {
		List<Integer> indexes = analyser.seekChars(c);
		indexes.forEach(index -> guessingWord[index] = c);
	}

	public boolean isWordCompleted() {
		for(char c: guessingWord) {
			if(c == '_')
				return false;
		}
		return true;
	}

}
