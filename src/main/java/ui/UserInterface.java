package ui;

public interface UserInterface {

	char getInputChar();

	void isGameWon(boolean b);

	void printExceptionMessage(Exception e);
	
	void printGuessingWord(char[] guessingWord);
	
	
}
