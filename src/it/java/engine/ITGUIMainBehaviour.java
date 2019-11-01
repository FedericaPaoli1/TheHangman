package engine;

import static org.assertj.core.api.Assertions.*;
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
		bh = new MainBehaviour(new MainExecutive(manager, controller), ui);
		window = new FrameFixture(robot(), ui);
		window.show();
	}

	@Test
	public void testMainBehaviourUpdatesViewWhenCharIsCorrect() {
		bh.executeControl('e');

		assertThat(window.label("finalWordChar1").target().getText()).isEqualTo("E");
	}

	@Test
	public void testWhenCharIsWrong() {
		bh.executeControl('a');

		assertThat(window.label("errorMessage").target().getText()).isEqualTo("The typed char is not present, please retry..");
		assertThat(window.textBox("missesTextBox").target().getText()).isEqualTo(" A");

		bh.executeControl('$');

		assertThat(window.label("errorMessage").target().getText()).isEqualTo("The typed char is not alphabetic, please retry with an alphabetic one.");
		assertThat(window.textBox("missesTextBox").target().getText()).isEqualTo(" A"); 
	}

	@Test
	public void testWinningMessageAppearsWhenGameIsWon() {
		// set the full array to ends gameLoop positively
		manager.setGuessingWord("test".toCharArray());
		bh.gameLoop();

		assertThat(window.label("gameResult").target().getText()).isEqualTo("Congratulations! YOU WON =)");
	}
	
}
