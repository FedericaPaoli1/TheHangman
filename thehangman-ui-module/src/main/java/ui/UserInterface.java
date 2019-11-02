package ui;

public interface UserInterface {

	char getInputChar();

	void isGameWon(boolean isWordCompleted);

	void printExceptionMessage(Exception e, char wrongChar);
	
	void printGuessingWord(char[] guessingWord);
}
