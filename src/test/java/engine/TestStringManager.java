package engine;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestStringManager {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	private StringManager manager;
	
	@Before
	public void setup() {
		manager = new StringManager("test");
	}
	
	@Test
	public void testGuessingWordIsEmptyWhenInputStringIsEmpty() {
		manager = new StringManager("");
		assertArrayEquals(new char[0], manager.getGuessingWord());
	}

	@Test
	public void testGuessingWordContainsAnUnderscoreWhenInputStringLengthIsOne() {
		manager = new StringManager("t");
		assertArrayEquals(new char[] {StringManager.EMPTY_CHAR}, manager.getGuessingWord());
	}
 
	@Test
	public void testGuessingWordContainsUnderscoresWhenInputStringLengthIsGreaterThanOne() {
		assertArrayEquals(new char[] {StringManager.EMPTY_CHAR, StringManager.EMPTY_CHAR, StringManager.EMPTY_CHAR, StringManager.EMPTY_CHAR}, manager.getGuessingWord());
	}

	
	@Test
	public void testUpdateGuessedWordWhenNumericCharShouldThrown() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid character");
		manager.updateGuessedWord('1');
	}
	
	@Test
	public void testUpdateGuessedWordWhenSpecialCharacterShouldThrown() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid character");
		manager.updateGuessedWord('$');
	}
	
	@Test
	public void testUpdateGuessedWordWhenCharNotAppears() {
		initializeEmptyGuessingWord(4);
		manager.updateGuessedWord('a');
		assertArrayEquals(new char[] {'_','_','_','_'}, manager.getGuessingWord());
	}

	private void initializeEmptyGuessingWord(int size) {
		char[] emptyGuessedWord = new char[size];
		Arrays.fill(emptyGuessedWord, StringManager.EMPTY_CHAR);
		manager.setGuessingWord(emptyGuessedWord);
	}

	@Test
	public void testUpdateGuessedWordWhenExactlyOneCharAppears() {
		initializeEmptyGuessingWord(4);
		manager.updateGuessedWord('e');
		assertArrayEquals(new char[] {'_','e','_','_'}, manager.getGuessingWord());
		
		manager = new StringManager("toast");
		manager.updateGuessedWord('o');
		assertArrayEquals(new char[] {'_','o','_','_','_'}, manager.getGuessingWord());
	}

	@Test 
	public void testUpdateGuessedWordWhenExactlyOneCharAppearsInADifferentPosition() {
		initializeEmptyGuessingWord(4);
		manager.updateGuessedWord('s');
		assertArrayEquals(new char[] {'_','_','s','_'}, manager.getGuessingWord());
	}
	
	@Test 
	public void testUpdateGuessedWordWhenExactlyOneCharAppearsInTwoPositions() {
		initializeEmptyGuessingWord(4);
		manager.updateGuessedWord('t');
		assertArrayEquals(new char[] {'t','_','_','t'}, manager.getGuessingWord());
	}
	
	@Test 
	public void testUpdateGuessedWordWhenExactlyOneCharAppearsInMoreThanTwoPositions() {
		manager = new StringManager("testt");
		initializeEmptyGuessingWord(5);
		manager.updateGuessedWord('t');
		assertArrayEquals(new char[] {'t','_','_','t','t'}, manager.getGuessingWord());
	}
}
