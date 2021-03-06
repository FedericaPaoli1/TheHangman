package ui;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import exceptions.CharAbsenceException;
import graphics.Stickman;

public class TerminalUI implements UserInterface {

	public static final String WINNING_MESSAGE = "Congratulations!\nYOU WON =)\n--------GAME OVER--------";
	public static final String LOOSING_MESSAGE = "OH NO!\nYou've finished your remaining attempts =(\n--------GAME OVER--------";

	private Scanner scanner;
	private int errorCounter;
	private List<Character> misses;
	private char[] guessingWord;

	public TerminalUI(Scanner scanner, int guessingWordLength) {
		this.scanner = scanner;
		this.misses = new LinkedList<>();
		this.guessingWord = new char[guessingWordLength];
		Arrays.fill(guessingWord, '_');
		printStatus(this.errorCounter);
	}

	private void printStatus(int figureIndex) {
		System.out.print("\nGuessing word: ");
		System.out.println(Arrays.toString(guessingWord)
				.toUpperCase()
				.replace("[", "").replace("]", "").replace(", ", " "));
		System.out.println(Arrays.toString(Stickman.IMMUTABLE_FIGURES.get(figureIndex)).replace("[", "|")
				.replace("]", "_").replace(", ", "\n"));
		System.out.print("MISSES: ");
		System.out.println(Arrays.toString(misses.toArray()).toUpperCase());
	}

	@Override
	public char getInputChar() {
		System.out.print("Insert a char: ");
		char c = (this.scanner.hasNext()) ? this.scanner.next().charAt(0) : ' ';
		return Character.toLowerCase(c);
	}

	@Override
	public void isGameWon(boolean isWordCompleted) {
		if (!isWordCompleted)
			System.out.println(LOOSING_MESSAGE);
		else
			System.out.println(WINNING_MESSAGE);

	}

	@Override
	public void printExceptionMessage(Exception e, char wrongChar) {
		System.out.println(e.getMessage());
		if (e instanceof CharAbsenceException) {
			this.errorCounter++;
			misses.add(wrongChar);
		}
		printStatus(this.errorCounter);
	}

	@Override
	public void printGuessingWord(char[] guessingWord) {
		this.guessingWord = guessingWord;
		printStatus(this.errorCounter);
	}


	int getErrorCounter() {
		return this.errorCounter;
	}

	void setErrorCounter(int i) {
		this.errorCounter = i;
	}

	List<Character> getMisses() {
		return this.misses;
	}

	void setMisses(List<Character> misses) {
		this.misses = misses;
	}

	char[] getGuessingWord() {
		return this.guessingWord;
	}

}