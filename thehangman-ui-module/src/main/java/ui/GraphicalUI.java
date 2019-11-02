package ui;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import exceptions.CharAbsenceException;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.Color;

public class GraphicalUI extends JFrame implements UserInterface {

	private static final Logger LOGGER = Logger.getLogger(GraphicalUI.class.getName());
	private static final String DIALOG_FONT = "Dialog";
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField charTextField;
	private JTextField missesTextField;
	private JLabel lblImage;
	private JLabel[] charLabels;
	private JButton btnTry;
	private JLabel lblMisses;
	private JLabel lblGameResult;
	private int errorCounter;
	private JLabel lblErrorMessage;
	private BlockingQueue<Character> queue = new ArrayBlockingQueue<>(1);

	public GraphicalUI(int guessingWordLength) {
		setTitle("The Hangman");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 950, 638);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 92, 180, 62, 62, 0, 40, 25, 12, 28, 0, 0, 0, 0, 0, 0, 22, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 17, 297, 0, 72, 49, 13, 39, 42, 39, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		charLabels = new JLabel[guessingWordLength];
		for (int i = 0; i < charLabels.length; i++) {
			JLabel lblChar = new JLabel(" ");
			lblChar.setName("finalWordChar" + i);
			lblChar.setFont(new Font(DIALOG_FONT, Font.BOLD, 40));
			lblChar.setForeground(Color.BLACK);
			lblChar.setBackground(new Color(255, 255, 255));
			lblChar.setBorder(new LineBorder(new Color(0, 0, 0), 2));
			GridBagConstraints gbc_lblChar = new GridBagConstraints();
			gbc_lblChar.anchor = GridBagConstraints.EAST;
			gbc_lblChar.insets = new Insets(0, 1, 0, 1);
			gbc_lblChar.gridx = 5 + i;
			gbc_lblChar.gridy = 2;
			contentPane.add(lblChar, gbc_lblChar);
			charLabels[i] = lblChar;
		}

		KeyAdapter btnEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnTry.setEnabled(!charTextField.getText().trim().isEmpty());
			}
		};

		lblImage = new JLabel("");
		lblImage.setName("image");
		lblImage.setIcon(new ImageIcon(GraphicalUI.class.getResource("/images/start.png")));
		GridBagConstraints gbc_lblImage = new GridBagConstraints();
		gbc_lblImage.fill = GridBagConstraints.VERTICAL;
		gbc_lblImage.gridwidth = 15;
		gbc_lblImage.anchor = GridBagConstraints.WEST;
		gbc_lblImage.insets = new Insets(0, 0, 5, 5);
		gbc_lblImage.gridx = 0;
		gbc_lblImage.gridy = 1;
		contentPane.add(lblImage, gbc_lblImage);
		
		lblGameResult = new JLabel(" ");
		lblGameResult.setName("gameResult");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.gridwidth = 10;
		gbc_lblNewLabel_1.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 15;
		gbc_lblNewLabel_1.gridy = 1;
		contentPane.add(lblGameResult, gbc_lblNewLabel_1);

		btnTry = new JButton("TRY");
		btnTry.setFont(new Font(DIALOG_FONT, Font.BOLD, 18));
		btnTry.setEnabled(false);
		GridBagConstraints gbc_btnTry = new GridBagConstraints();
		gbc_btnTry.anchor = GridBagConstraints.EAST;
		gbc_btnTry.insets = new Insets(0, 0, 5, 5);
		gbc_btnTry.gridx = 1;
		gbc_btnTry.gridy = 4;
		contentPane.add(btnTry, gbc_btnTry);
		btnTry.addActionListener(e -> {
				boolean result = queue.offer(charTextField.getText().toLowerCase().trim().charAt(0));
				this.lblErrorMessage.setText(" ");
				this.charTextField.setText("");
				this.btnTry.setEnabled(false);
				LOGGER.info("Offer result: " + result);
			
		});

		charTextField = new JTextField();
		charTextField.setName("charTextBox");
		charTextField.setText(" ");
		GridBagConstraints gbc_charTextField = new GridBagConstraints();
		gbc_charTextField.fill = GridBagConstraints.BOTH;
		gbc_charTextField.insets = new Insets(0, 0, 5, 5);
		gbc_charTextField.gridx = 2;
		gbc_charTextField.gridy = 4;
		contentPane.add(charTextField, gbc_charTextField);
		charTextField.setColumns(10);
		charTextField.addKeyListener(btnEnabler);

		lblMisses = new JLabel("MISSES: ");
		lblMisses.setFont(new Font(DIALOG_FONT, Font.BOLD, 18));
		lblMisses.setName("misses");
		GridBagConstraints gbc_lblMisses = new GridBagConstraints();
		gbc_lblMisses.gridwidth = 2;
		gbc_lblMisses.gridheight = 2;
		gbc_lblMisses.anchor = GridBagConstraints.SOUTHEAST;
		gbc_lblMisses.insets = new Insets(0, 0, 5, 5);
		gbc_lblMisses.gridx = 0;
		gbc_lblMisses.gridy = 5;
		contentPane.add(lblMisses, gbc_lblMisses);

		missesTextField = new JTextField();
		missesTextField.setFont(new Font(DIALOG_FONT, Font.PLAIN, 15));
		missesTextField.setName("missesTextBox");
		missesTextField.setEditable(false);
		missesTextField.setText("");
		GridBagConstraints gbc_missesTextField = new GridBagConstraints();
		gbc_missesTextField.gridwidth = 3;
		gbc_missesTextField.insets = new Insets(0, 0, 5, 5);
		gbc_missesTextField.fill = GridBagConstraints.BOTH;
		gbc_missesTextField.gridx = 2;
		gbc_missesTextField.gridy = 6;
		contentPane.add(missesTextField, gbc_missesTextField);
		missesTextField.setColumns(10);

		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setName("errorMessage");
		GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
		gbc_lblErrorMessage.insets = new Insets(0, 0, 5, 5);
		gbc_lblErrorMessage.gridwidth = 16;
		gbc_lblErrorMessage.gridx = 0;
		gbc_lblErrorMessage.gridy = 7;
		contentPane.add(lblErrorMessage, gbc_lblErrorMessage);

	}

	@Override
	public char getInputChar() {
		return takeElementFromQueue();
	}

	private char takeElementFromQueue() {
		char c = ' ';
		try {
			c = queue.take();
		} catch (InterruptedException e) {
		    Thread.currentThread().interrupt();
		    throw new IllegalStateException(e);
		}
		return c;
	}

	@Override
	public void isGameWon(boolean isWordCompleted) {
		if (!isWordCompleted)
			SwingUtilities
					.invokeLater(() -> lblGameResult.setText("OH NO! You've finished your remaining attempts =("));
		else
			SwingUtilities.invokeLater(() -> lblGameResult.setText("Congratulations! YOU WON =)"));
	}

	@Override
	public void printExceptionMessage(Exception e, char wrongChar) {
		SwingUtilities.invokeLater(() -> lblErrorMessage.setText(e.getMessage()));
		if (e instanceof CharAbsenceException) {
			this.errorCounter++;
			SwingUtilities.invokeLater(() -> missesTextField
					.setText((missesTextField.getText() + " " + Character.toUpperCase(wrongChar))));
			SwingUtilities.invokeLater(() -> lblImage.setIcon(
					new ImageIcon(GraphicalUI.class.getResource("/images/error_" + this.errorCounter + ".png"))));
		}
	}

	@Override
	public void printGuessingWord(char[] guessingWord) {
		SwingUtilities.invokeLater(() -> {
			for (int i = 0; i < guessingWord.length; i++) {
				if (guessingWord[i] != '_')
					charLabels[i].setText("" + Character.toUpperCase(guessingWord[i]));
			}
		});
	}

	int getErrorCounter() {
		return this.errorCounter;
	}

	void setErrorCounter(int i) {
		this.errorCounter = i;
	}

	BlockingQueue<Character> getQueue() {
		return this.queue;
	}
}
