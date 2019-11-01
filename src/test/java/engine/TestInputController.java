package engine;

import static org.assertj.core.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import exceptions.NotAlphabeticCharException;

public class TestInputController {

	private static final String ERROR_MESSAGE = "The typed char is not alphabetic, please retry with an alphabetic one";
	private static final String WORD_TO_GUESS = "test";
	private InputController controller;
	private List<Character> alreadyTyped;

	@Before
	public void setup() {
		controller = new InputController(WORD_TO_GUESS);
		alreadyTyped = new LinkedList<Character>();
		controller.setAlreadyTyped(alreadyTyped);
	}

	@Test
	public void testIsPresentWhenCharNotAppear() {
		assertThat(controller.isPresent('a')).isFalse();
	}

	@Test
	public void testIsPresentWhenCharAppears() {
		assertThat(controller.isPresent('e')).isTrue();
	}

	@Test
	public void testIsPresentWhenFinalWordIsEmpty() {
		controller = new InputController("");
		assertThat(controller.isPresent('e')).isFalse();
	}

	@Test
	public void testIsAlreadyTypedWhenItIsTheFirstTime() throws Exception {
		assertThat(controller.isAlreadyTyped('a')).isFalse();
		assertThat(alreadyTyped.contains('a')).isTrue();
	}

	@Test
	public void testIsAlreadyTypedWhenItIsTheFirstTimeOfAnotherChar() throws Exception {
		assertThat(controller.isAlreadyTyped('e')).isFalse();
		assertThat(alreadyTyped.contains('e')).isTrue();
	}

	@Test
	public void testIsAlreadyTypedWhenItIsAlreadyPresent() throws Exception {
		char charToTest = 'e';
		alreadyTyped.add(charToTest);

		assertThat(controller.isAlreadyTyped(charToTest)).isTrue();
		assertThat(alreadyTyped.size() == 1).isTrue();
		assertThat(alreadyTyped.contains(charToTest)).isTrue();
	}

	@Test
	public void testIsAlreadyTypedWhenNumericCharShouldThrown() throws Exception {
		assertThatThrownBy(() -> controller.isAlreadyTyped('1'))
			.isInstanceOf(NotAlphabeticCharException.class)
			.as(ERROR_MESSAGE);
	}

	@Test
	public void testIsAlreadyTypedWhenSpecialCharacterShouldThrown() throws Exception {
		assertThatThrownBy(() -> controller.isAlreadyTyped('$'))
			.isInstanceOf(NotAlphabeticCharException.class)
			.as(ERROR_MESSAGE);
	}

}
