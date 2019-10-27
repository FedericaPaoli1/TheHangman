package engine;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import exceptions.NotAlphabeticCharException;

public class TestInputController {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

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
		assertFalse(controller.isPresent('a'));
	}

	@Test
	public void testIsPresentWhenCharAppears() {
		assertTrue(controller.isPresent('e'));
	}

	@Test
	public void testIsPresentWhenFinalWordIsEmpty() {
		controller = new InputController("");
		assertFalse(controller.isPresent('e'));
	}

	@Test
	public void testIsAlreadyTypedWhenItIsTheFirstTime() throws Exception {
		assertFalse(controller.isAlreadyTyped('a'));
		assertTrue(alreadyTyped.contains('a'));
	}

	@Test
	public void testIsAlreadyTypedWhenItIsTheFirstTimeOfAnotherChar() throws Exception {
		assertFalse(controller.isAlreadyTyped('e'));
		assertTrue(alreadyTyped.contains('e'));
	}

	@Test
	public void testIsAlreadyTypedWhenItIsAlreadyPresent() throws Exception {
		char charToTest = 'e';
		alreadyTyped.add(charToTest);

		assertTrue(controller.isAlreadyTyped(charToTest));
		assertTrue(alreadyTyped.size() == 1);
		assertTrue(alreadyTyped.contains(charToTest));
	}

	@Test
	public void testIsAlreadyTypedWhenNumericCharShouldThrown() throws Exception {
		thrown.expect(NotAlphabeticCharException.class);
		thrown.expectMessage("The typed char is not alphabetic, please retry with an alphabetic one");
		controller.isAlreadyTyped('1');
	}

	@Test
	public void testIsAlreadyTypedWhenSpecialCharacterShouldThrown() throws Exception {
		thrown.expect(NotAlphabeticCharException.class);
		thrown.expectMessage("The typed char is not alphabetic, please retry with an alphabetic one");
		controller.isAlreadyTyped('$');
	}

}
