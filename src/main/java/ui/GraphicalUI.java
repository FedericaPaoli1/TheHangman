package ui;

import java.awt.EventQueue;
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

import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Color;

public class GraphicalUI extends JFrame implements UserInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField charTextField;
	private JTextField missesTextField;
	private JLabel lblImage;
	private JLabel[] charLabels;
	private JButton btnTry;
	private JLabel lblMisses;
	private JLabel lblGameResult;
	private JLabel lblErrorMessage;
	private int errorCounter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphicalUI frame = new GraphicalUI(Integer.parseInt(args[0]));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GraphicalUI(int guessingWordLength) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 950, 638);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 440, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 317, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		lblGameResult = new JLabel(" ");
		lblGameResult.setName("gameResult");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblGameResult, gbc_lblNewLabel);

		lblImage = new JLabel("");
		lblImage.setName("image");
		lblImage.setIcon(new ImageIcon(GraphicalUI.class.getResource("/images/start.png")));
		GridBagConstraints gbc_lblImage = new GridBagConstraints();
		gbc_lblImage.insets = new Insets(0, 0, 5, 5);
		gbc_lblImage.anchor = GridBagConstraints.NORTH;
		gbc_lblImage.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblImage.gridx = 0;
		gbc_lblImage.gridy = 1;
		contentPane.add(lblImage, gbc_lblImage);

		charLabels = new JLabel[guessingWordLength];
		for (int i = 0; i < charLabels.length; i++) {
			JLabel lblChar = new JLabel(" ");
			lblChar.setName("finalWordChar" + i);
			lblChar.setFont(new Font("Dialog", Font.BOLD, 40));
			lblChar.setForeground(Color.BLACK);
			lblChar.setBackground(new Color(255, 255, 255));
			lblChar.setBorder(new LineBorder(new Color(0, 0, 0), 2));
			GridBagConstraints gbc_lblChar = new GridBagConstraints();
			gbc_lblChar.insets = new Insets(0, 0, 5, i * 50);
			gbc_lblChar.ipadx = 1;
			gbc_lblChar.gridx = 0;
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

		btnTry = new JButton("TRY");
		btnTry.setEnabled(false);
		GridBagConstraints gbc_btnTry = new GridBagConstraints();
		gbc_btnTry.insets = new Insets(0, 0, 5, 5);
		gbc_btnTry.gridx = 0;
		gbc_btnTry.gridy = 4;
		contentPane.add(btnTry, gbc_btnTry);

		charTextField = new JTextField();
		charTextField.setName("charTextBox");
		charTextField.setText(" ");
		GridBagConstraints gbc_charTextField = new GridBagConstraints();
		gbc_charTextField.insets = new Insets(0, 0, 5, 0);
		gbc_charTextField.anchor = GridBagConstraints.WEST;
		gbc_charTextField.gridx = 2;
		gbc_charTextField.gridy = 5;
		contentPane.add(charTextField, gbc_charTextField);
		charTextField.setColumns(10);
		charTextField.addKeyListener(btnEnabler);

		lblMisses = new JLabel("MISSES: ");
		lblMisses.setName("misses");
		GridBagConstraints gbc_lblMisses = new GridBagConstraints();
		gbc_lblMisses.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblMisses.anchor = GridBagConstraints.SOUTH;
		gbc_lblMisses.insets = new Insets(0, 0, 5, 5);
		gbc_lblMisses.gridx = 0;
		gbc_lblMisses.gridy = 7;
		contentPane.add(lblMisses, gbc_lblMisses);

		missesTextField = new JTextField();
		missesTextField.setName("missesTextBox");
		missesTextField.setEditable(false);
		missesTextField.setText(" ");
		GridBagConstraints gbc_missesTextField = new GridBagConstraints();
		gbc_missesTextField.insets = new Insets(0, 0, 5, 0);
		gbc_missesTextField.fill = GridBagConstraints.VERTICAL;
		gbc_missesTextField.anchor = GridBagConstraints.WEST;
		gbc_missesTextField.gridx = 2;
		gbc_missesTextField.gridy = 8;
		contentPane.add(missesTextField, gbc_missesTextField);
		missesTextField.setColumns(10);

		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setName("errorMessage");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 10;
		contentPane.add(lblErrorMessage, gbc_label);

	}

	@Override
	public char getInputChar() {
		return charTextField.getText().trim().charAt(0);
	}

	@Override
	public void isGameWon(boolean isWordCompleted) {
		if (!isWordCompleted)
			lblGameResult.setText("OH NO! You've finished your remaining attempts =(");
		else
			lblGameResult.setText("Congratulations! YOU WON =)");
	}

	@Override
	public void printExceptionMessage(Exception e) {
		lblErrorMessage.setText(e.getMessage());
		if (e instanceof CharAbsenceException) {
			this.errorCounter++;
			lblImage.setIcon(
					new ImageIcon(GraphicalUI.class.getResource("/images/error_" + this.errorCounter + ".png")));
		}
	}

	@Override
	public void printGuessingWord(char[] guessingWord) {
		for (int i = 0; i < guessingWord.length; i++) {
			if(guessingWord[i] != '_') 
				charLabels[i].setText(("" + guessingWord[i]).toUpperCase());
		}
	}

	int getErrorCounter() {
		return this.errorCounter;
	}

	public void setErrorCounter(int i) {
		this.errorCounter = i;
	}

	JLabel getLblImage() {
		return this.lblImage;
	}
}