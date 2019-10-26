package engine;


import exceptions.CharAbsenceException;
import exceptions.IllegalCharException;
import ui.TerminalUI;
import ui.UserInterface;

public class MainBehaviour {

	/*
	 * test visibility
	 */
	static final int MAX_ERROR_NUMBER = 6;
	
	private MainExecutive executive;
	private int errorCounter;
	private UserInterface ui;

	public MainBehaviour(MainExecutive executive, UserInterface ui) {
		this.executive = executive;
		this.ui = ui;
	}

	public void executeControl(char c) {
		try {
			char[] guessingWord = executive.performCharControl(c);
			ui.printGuessingWord(guessingWord);
		} catch (CharAbsenceException charAbs) {
			errorCounter++;
			ui.printExceptionMessage(charAbs, c);
		} catch (IllegalCharException illChar) {
			ui.printExceptionMessage(illChar, c);
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
		boolean isLimitNumber = errorCounter >= MAX_ERROR_NUMBER;
		return isCompleted || isLimitNumber;
	}
	
/*	public static void main(String[] args) {
		UserInterface terminal = new TerminalUI(System.in, 4);
		MainExecutive exec = new MainExecutive("test", new StringManager("test"), new InputController("test"));
		MainBehaviour behav = new MainBehaviour(exec, terminal);
		behav.gameLoop();
	}*/
}