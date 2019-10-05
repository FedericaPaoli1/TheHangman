package engine;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestStringAnalyser {
	
/*	@Rule
	public ExpectedException thrown = ExpectedException.none();*/
	
	private StringAnalyser analyser;
	
	@Before
	public void setup() {
		analyser = new StringAnalyser("test");
	}

	@Test
	public void testSeekCharsWhenCharNotAppears() {
		List<Integer> result = analyser.seekChars('a');
		assertTrue("Unexpected char found", result.isEmpty());
	}
	
	@Test
	public void testSeekCharsWhenExactlyOneCharAppears() {
		String string  = analyser.getString();
		List<Integer> result = analyser.seekChars('e');
		assertTrue(result.contains(string.indexOf('e')));
		assertEquals(1, result.size());
	}
	
	@Test
	public void testSeekCharsWhenCharAppearsTwice() {
		List<Integer> result = analyser.seekChars('t');
		int firstIndex = 0, lastIndex = 3;
		assertTrue(result.contains(firstIndex));
		assertTrue(result.contains(lastIndex));
		assertEquals(2, result.size());
	}
	
	// Documentation purpose
	@Test
	public void testSeekCharsWhenCharAppearsSeveralTimes() {
		analyser = new StringAnalyser("bbbb");
		List<Integer> result = analyser.seekChars('b');
		int stringLength = analyser.getString().length();
		for(int i = 0; i < stringLength; i++) {
			assertTrue(result.contains((i)));
		}
		assertEquals(stringLength, result.size());
	}
	
/*	@Test
	public void testSeekCharsWhenNumericCharShouldThrown() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid character");
		analyser.seekChars('1');
	}
	
	@Test
	public void testSeekCharsWhenSpecialCharacterShouldThrown() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid character");
		analyser.seekChars('$');
	}*/
 

}
