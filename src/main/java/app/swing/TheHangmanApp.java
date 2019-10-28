package app.swing;

import java.awt.EventQueue;
import java.util.Scanner;
import java.util.concurrent.Callable;

import org.hibernate.cfg.Configuration;

import engine.InputController;
import engine.MainBehaviour;
import engine.MainExecutive;
import engine.StringManager;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import repository.ConfigurationBuilder;
import repository.WordMySQLRepository;
import ui.GraphicalUI;
import ui.TerminalUI;
import ui.UserInterface;

@Command(mixinStandardHelpOptions = true)
public class TheHangmanApp implements Callable<Void> {

	private static class GraphicalExecution {

		static Runnable createUi(String mode, String finalWord) {

			if (mode.equals("terminal")) {
				return new TerminalRunnable(finalWord);
			}

			return new GraphicalRunnable(finalWord);
		}
	}

	private static class TerminalRunnable implements Runnable {

		String finalWord;

		TerminalRunnable(String finalWord) {
			this.finalWord = finalWord;

		}

		@Override
		public void run() {
			UserInterface ui = new TerminalUI(new Scanner(System.in), finalWord.length());
			MainBehaviour behaviour = new MainBehaviour(
					new MainExecutive(finalWord, new StringManager(finalWord), new InputController(finalWord)), ui);
			behaviour.gameLoop();
		}

	}

	private static class GraphicalRunnable implements Runnable {

		String finalWord;

		GraphicalRunnable(String finalWord) {
			this.finalWord = finalWord;
		}

		@Override
		public void run() {
			EventQueue.invokeLater(() -> {
				GraphicalUI ui = new GraphicalUI(finalWord.length());
				ui.setVisible(true);
				MainBehaviour behaviour = new MainBehaviour(
						new MainExecutive(finalWord, new StringManager(finalWord), new InputController(finalWord)), ui);
				new Thread(() -> behaviour.gameLoop()).start();
			});
		}

	}

	@Option(names = { "--mysql-port" }, description = "mysql host port")
	private int mysqlPort = 3306;

	@Option(names = { "--mysql-username" }, description = "mysql username")
	private String mysqlUsername = "root";

	@Option(names = { "--mysql-password" }, description = "mysql password")
	private String mysqlPassword = "root";

	@Option(names = { "--db-name" }, description = "database name")
	private String databaseName = "HangmanDB";

	@Option(names = { "mode" }, description = "game mode")
	private String mode = "graphical";

	@Option(names = "-t")
	private boolean isTestMode;

	public static void main(String[] args) {
		CommandLine.call(new TheHangmanApp(), args);
	}

	@Override
	public Void call() throws Exception {
		try {
			Configuration conf = new ConfigurationBuilder().withUsername(this.mysqlUsername)
					.withPassword(this.mysqlPassword).withExposedPort("" + this.mysqlPort)
					.withDatabaseName(this.databaseName).build();
			loadTestScript(isTestMode, conf);
			System.out.println("Congiguration created");
			WordMySQLRepository repository = new WordMySQLRepository(conf.buildSessionFactory());
			String finalWord = repository.getRandomWord();
			System.out.println("Taked andom work");
//			UserInterface ui = chooseUserInterface(this.mode, finalWord.length());
//			System.out.println("Interface");
//			MainBehaviour behaviour = new MainBehaviour(
//					new MainExecutive(finalWord, new StringManager(finalWord), new InputController(finalWord)), ui);
//			System.out.println("Main behaviour");
//			new Thread(() -> behaviour.gameLoop()).start();
			GraphicalExecution.createUi(this.mode, finalWord).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("End invoke");
		return null;
	}

	private static UserInterface chooseUserInterface(String mode, int finalWordLength) {
		if (mode.equals("graphical")) {
			GraphicalUI gui = new GraphicalUI(finalWordLength);
			gui.setVisible(true);
			return gui;
		} else if (mode.equals("terminal"))
			return new TerminalUI(new Scanner(System.in), finalWordLength);
		else
			throw new IllegalArgumentException();
	}

	private static Configuration loadTestScript(boolean isTest, Configuration conf) {
		if (isTest)
			conf.setProperty("javax.persistence.sql-load-script-source", "src/test/resources/testLoad.sql");
		return conf;
	}

}
