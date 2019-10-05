package engine;

import static org.junit.Assert.*;

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
	
/*	@Test
	public void testUpdateGuessedWordWhenCharNotAppears() {
		manager.setGuessingWord(new char[] {'_','_','_','_'});
		manager.updateGuessedWord('a');
		assertArrayEquals(new char[] {'_','_','_','_'}, manager.getGuessingWord());
	}

	@Test
	public void testUpdateGuessedWordWhenACharAppears() {
		manager.setGuessingWord(new char[] {'_','_','_','_'});
		manager.updateGuessedWord('e');
		assertArrayEquals(new char[] {'_','e','_','_'}, manager.getGuessingWord());
	}*/


}
