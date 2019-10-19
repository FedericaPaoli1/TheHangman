package ui;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

import exceptions.CharAbsenceException;
import graphics.Stickman;

public class TerminalUI implements UserInterface {

	private InputStream in;
	private int errorCounter;

	public TerminalUI(InputStream in, int guessingWordLength) {
		this.in = in;
		char[] guessingWord = new char[guessingWordLength];
		System.out.println("\nGuessing word:");
		Arrays.fill(guessingWord, '_');
		printGuessingWord(guessingWord);
		this.errorCounter = 0;
		printStatus(this.errorCounter);
	}

	@Override
	public char getInputChar() {
		Scanner scanner = new Scanner(in);
		System.out.print("Insert a char: ");
		char c = (scanner.hasNext()) ? scanner.next().charAt(0) : ' ';
		return c;
	}

	@Override
	public void isGameWon(boolean isWordCompleted) {
		if (isWordCompleted == false)
			System.out.println("OH NO!\nYou've finished your remaining attempts =(\n--------GAME OVER--------");
		else
			System.out.println("Congratulations!\nYOU WON =)\n--------GAME OVER--------");

	}

	@Override
	public void printExceptionMessage(Exception e) {
		System.out.println(e.getMessage());
		if (e instanceof CharAbsenceException) {
			this.errorCounter++;
			printStatus(this.errorCounter);
		}
	}

	@Override
	public void printGuessingWord(char[] guessingWord) {
		System.out.println(Arrays.toString(guessingWord).replace("[", "").replace("]", "").replace(", ", " "));
	}

	public void printStatus(int figureIndex) {
		System.out.println(
				Arrays.toString(Stickman.FIGURES[figureIndex]).replace("[", "").replace("]", "").replace(", ", "\n"));
	}

	public int getErrorCounter() {
		return this.errorCounter;
	}

	public void setErrorCounter(int i) {
		this.errorCounter = i;
	}

}
