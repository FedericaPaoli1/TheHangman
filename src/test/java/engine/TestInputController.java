package engine;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestInputController {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static final String WORD_TO_GUESS = "test";
	private InputController controller;
	private List<Character> alreadyTyped; 

	@Before 
	public void setup()
	{
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
	public void testIsAlreadyTypedWhenItIsTheFirstTime() {
		assertFalse(controller.isAlreadyTyped('a'));
		assertTrue(alreadyTyped.contains('a'));
	}
	
	@Test
	public void testIsAlreadyTypedWhenItIsTheFirstTimeOfAnotherChar() {
		assertFalse(controller.isAlreadyTyped('e'));
		assertTrue(alreadyTyped.contains('e'));
	}
	
	@Test
	public void testIsAlreadyTypedWhenItIsAlreadyPresent() 
	{
		char charToTest = 'e';
		alreadyTyped.add(charToTest);
		
		assertTrue(controller.isAlreadyTyped(charToTest));
		assertTrue(alreadyTyped.size() == 1);
		assertTrue(alreadyTyped.contains(charToTest));
	}
	
	@Test
	public void testIsAlreadyTypedWhenNumericCharShouldThrown() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid character");
		controller.isAlreadyTyped('1');
		//assertFalse(alreadyTyped.contains('1'));
	}
	
	@Test
	public void testIsAlreadyTypedWhenSpecialCharacterShouldThrown() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid character");
		controller.isAlreadyTyped('$');
		//assertFalse(alreadyTyped.contains('1'));
	}

}
