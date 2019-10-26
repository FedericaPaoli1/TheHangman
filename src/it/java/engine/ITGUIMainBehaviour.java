package engine;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import ui.GraphicalUI;

public class ITGUIMainBehaviour extends AssertJSwingJUnitTestCase {

	private static final String STRING_UNDER_TEST = "test";
	private GraphicalUI ui;

	private MainBehaviour bh;
	private FrameFixture window;
	private StringManager manager;
	private InputController controller;
	

	@Override
	protected void onSetUp() {
		GuiActionRunner.execute(() -> {
			ui = new GraphicalUI(STRING_UNDER_TEST.length());
		});
		manager = new StringManager(STRING_UNDER_TEST);
		controller = new InputController(STRING_UNDER_TEST);
		bh = new MainBehaviour(new MainExecutive(STRING_UNDER_TEST, manager, controller), ui);
		window = new FrameFixture(robot(), ui);
		window.show();
	}

	@Test
	public void testMainBehaviourUpdatesViewWhenCharIsCorrect() {
		bh.executeControl('e');

		window.label("finalWordChar1").requireText("E");
	}

	@Test
	public void testWhenCharIsWrong() {
		bh.executeControl('a');

		window.label("errorMessage").requireText("Char not present");
		window.textBox("missesTextBox").requireText(" a");

		bh.executeControl('$');

		window.label("errorMessage").requireText("Char is not alphabetic");
		window.textBox("missesTextBox").requireText(" a"); //unchanged
	}

	@Test
	public void testWinningMessageAppearsWhenGameIsWon() {
		// set the full array to ends gameLoop positively
		manager.setGuessingWord("test".toCharArray());
		bh.gameLoop();

		window.label("gameResult").requireText("Congratulations! YOU WON =)");
	}
	
}
