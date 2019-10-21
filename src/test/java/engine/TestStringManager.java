package engine;

import static engine.StringManager.EMPTY_CHAR;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class TestStringManager {

	private static final String STRING_UNDER_TEST = "test";
	private static final int SIZE_OF_TEST_WORD = 4;
	private StringManager manager;

	@Before
	public void setup() {
		manager = new StringManager(STRING_UNDER_TEST);
	}

	@Test
	public void testGuessingWordIsEmptyWhenInputStringIsEmpty() {
		manager = new StringManager("");
		assertThat(manager.getGuessingWord()).isEmpty();
	}

	@Test
	public void testGuessingWordContainsAnUnderscoreWhenInputStringLengthIsOne() {
		manager = new StringManager("t");
		assertThat(manager.getGuessingWord()).containsOnlyOnce(EMPTY_CHAR);
	}

	@Test
	public void testGuessingWordContainsUnderscoresWhenInputStringLengthIsGreaterThanOne() {
		assertThat(manager.getGuessingWord()) 
				.hasSize(4).containsOnly(EMPTY_CHAR);
	}

	@Test
	public void testUpdateGuessedWordWhenCharNotAppears() {
		initializeEmptyGuessingWord(SIZE_OF_TEST_WORD);
		char notPresent = 'a';

		manager.updateGuessedWord(notPresent);

		assertThat(manager.getGuessingWord()).containsOnly(EMPTY_CHAR);
	}

	private void initializeEmptyGuessingWord(int size) {
		char[] emptyGuessedWord = new char[size];
		Arrays.fill(emptyGuessedWord, EMPTY_CHAR);
		manager.setGuessingWord(emptyGuessedWord);
	}

	@Test
	public void testUpdateGuessedWordWhenExactlyOneCharAppears() {
		initializeEmptyGuessingWord(SIZE_OF_TEST_WORD);
		char presentChar = 'e';
		final char[] emptySubsequence = new char[] { EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR };

		manager.updateGuessedWord(presentChar);

		assertThat(manager.getGuessingWord())
				.containsOnlyOnce(presentChar)
				.contains(presentChar, atIndex(1))
				.containsSubsequence(emptySubsequence);

		manager = new StringManager("toast");
		presentChar = 'o';

		manager.updateGuessedWord(presentChar);

		assertThat(manager.getGuessingWord())
				.containsOnlyOnce(presentChar)
				.contains(presentChar, atIndex(1))
				.containsSubsequence(emptySubsequence);
	}

	@Test
	public void testUpdateGuessedWordWhenExactlyOneCharAppearsInADifferentPosition() {
		initializeEmptyGuessingWord(4);
		final char[] emptySubsequence = new char[] { EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR };

		char presentChar = 's';
		manager.updateGuessedWord(presentChar);

		assertThat(manager.getGuessingWord())
				.containsOnlyOnce(presentChar)
				.contains(presentChar, atIndex(2))
				.containsSubsequence(emptySubsequence);

	}

	@Test
	public void testUpdateGuessedWordWhenExactlyOneCharAppearsInTwoPositions() {
		initializeEmptyGuessingWord(SIZE_OF_TEST_WORD);
		char presentChar = 't';

		manager.updateGuessedWord(presentChar);

		assertThat(manager.getGuessingWord())
				.contains(presentChar, atIndex(0))
				.contains(presentChar, atIndex(3))
				.containsSubsequence(new char[] { EMPTY_CHAR, EMPTY_CHAR });
	}

	@Test
	public void testUpdateGuessedWordWhenExactlyOneCharAppearsInMoreThanTwoPositions() {
		String word = "testt";
		manager = new StringManager(word);
		initializeEmptyGuessingWord(word.length());

		manager.updateGuessedWord('t');

		assertThat(manager.getGuessingWord()).isEqualTo(new char[] { 't', '_', '_', 't', 't' });
	}

	@Test
	public void testIsWordCompletedWhenNoEmptyCharAppears() {
		manager.setGuessingWord(new char[] { 't', 'e', 's', 't' });

		assertThat(manager.isWordCompleted()).isTrue();
	}

	@Test
	public void testIsWordCompletedWhenAnEmptyCharAppears() {
		manager.setGuessingWord(new char[] { 't', 'e', '_', 't' });
		
		assertThat(manager.isWordCompleted()).isFalse();
	}

	@Test
	public void testIsWordCompletedWhenSeveralEmptyCharAppear() {
		manager.setGuessingWord(new char[] { '_', 'e', 's', '_' });
		
		assertThat(manager.isWordCompleted()).isFalse();
	}

}
