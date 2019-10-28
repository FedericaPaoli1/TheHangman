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

	@Option(names = "-t" )
	private boolean isTestMode;

	public static void main(String[] args) {
		CommandLine.call(new TheHangmanApp(), args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				Configuration conf = new ConfigurationBuilder()
						.withUsername(this.mysqlUsername)
						.withPassword(this.mysqlPassword)
						.withExposedPort("" + this.mysqlPort)
						.withDatabaseName(this.databaseName)
						.build();
				loadTestScript(isTestMode, conf);
				WordMySQLRepository repository = new WordMySQLRepository(conf.buildSessionFactory());
				String finalWord = repository.getRandomWord();
				UserInterface ui = chooseUserInterface(this.mode, finalWord.length());
				MainBehaviour behaviour = new MainBehaviour(
						new MainExecutive(finalWord, 
								new StringManager(finalWord), 
								new InputController(finalWord)), 
						ui);
				new Thread(() -> behaviour.gameLoop()).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return null;
	}

	private static UserInterface chooseUserInterface(String mode, int finalWordLength) {
		if (mode.equals("graphical")) {
			GraphicalUI gui = new GraphicalUI(finalWordLength);
			gui.setVisible(true);
			return gui;
		}
		else if (mode.equals("terminal"))
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
