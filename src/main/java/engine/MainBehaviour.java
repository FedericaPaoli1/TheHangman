package engine;

import java.util.Arrays;

import exceptions.CharAbsenceException;
import exceptions.IllegalCharException;
import ui.UserInterface;

public class MainBehaviour {

	private MainExecutive executive;
	private int errorCounter;
	private UserInterface ui;

	public MainBehaviour() {
	}

	MainBehaviour(MainExecutive executive, UserInterface ui) {
		this.executive = executive;
		this.ui = ui;
	}

	// TO DELETE
	public MainBehaviour(MainExecutive executive2) {
		this.executive = executive2;
	}

	public void executeControl(char c) {
		try {
			char[] guessingWord = executive.performCharControl(c);
			System.out.println(Arrays.toString(guessingWord));
		} catch (CharAbsenceException charAbs) {
			errorCounter++;
			System.out.println(charAbs.getMessage());
		} catch (IllegalCharException illChar) {
			System.out.println(illChar.getMessage());
		}
	}

	public int getErrorCounter() {
		return this.errorCounter;
	}

	void setErrorCounter(int errorCounter) {
		this.errorCounter = errorCounter;
	}

	public void gameLoop() {
		while (!isGameEnded()) {
			char inputChar = ui.getInputChar();
			executeControl(inputChar);
		}
		ui.isGameWon(executive.isWordCompleted());
	}

	public boolean isGameEnded() {
		boolean isCompleted = executive.isWordCompleted();
		boolean isLimitNumber = errorCounter >= 6;
		return isCompleted || isLimitNumber;
	}

}