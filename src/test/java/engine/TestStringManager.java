package engine;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class TestStringManager {
	
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
	
	// Nel REFACTORING => eliminare StringAnalyser e mettere tutto in StringManager

	@Test 
	public void testUpdateGuessedWordWhenExactlyOneCharAppearsInMoreThanTwoPositionsMOCK() {
		StringAnalyser analyser = spy(new StringAnalyser("testt"));
		manager = new StringManager("testt");
		manager.setAnalyser(analyser);
		initializeEmptyGuessingWord(5);
		
		doReturn(Arrays.asList(0, 3, 4)).when(analyser).seekChars('t');
		
		manager.updateGuessedWord('t');
		
		assertArrayEquals(new char[] {'t','_','_','t','t'}, manager.getGuessingWord());
		verify(analyser).seekChars('t');
	}
	
	@Test
	public void testIsWordCompletedWhenNoEmptyCharAppears() {
		manager.setGuessingWord(new char[] {'t','e','s','t'});
		assertThat(manager.isWordCompleted()).isTrue();
	}
	
	@Test
	public void testIsWordCompletedWhenAnEmptyCharAppears() {
		manager.setGuessingWord(new char[] {'t','e','_','t'});
		assertThat(manager.isWordCompleted()).isFalse();
	}

	@Test
	public void testIsWordCompletedWhenSeveralEmptyCharAppear() {
		manager.setGuessingWord(new char[] {'_','e','s','_'});
		assertThat(manager.isWordCompleted()).isFalse();
	}
	
}
