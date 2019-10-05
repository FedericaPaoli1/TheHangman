package engine;

import java.util.ArrayList;
import java.util.List;

public class StringAnalyser {

	private String string;

	public StringAnalyser(String string) {
		this.string = string;
	}

	public List<Integer> seekChars(char c) {
		List<Integer> indexes = new ArrayList<Integer>();

	/*	if (!Character.isAlphabetic(c))
			throw new IllegalArgumentException("Invalid character");*/

		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == c)
				indexes.add(i);
		}

		return indexes;
	}

	String getString() {
		return this.string;
	}

}