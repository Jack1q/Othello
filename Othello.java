import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Othello {

	private static Othello window;

	private JFrame choiceFrame;
	private JFrame gameFrame;
	private JFrame difficultyFrame;
	private JButton[][] gridOfSquares;

	private InternalLogic internalLogic;

	private final String GREEN_SQUARE_IMAGE_PATH = "/images/green_square.jpg";
	private final String WHITE_PIECE_IMAGE_PATH = "/images/white_piece.jpg";
	private final String BLACK_PIECE_IMAGE_PATH = "/images/black_piece.jpg";
	private final String POSSIBLE_MOVE_IMAGE_PATH = "/images/possible_move.jpg";

	private int currentPlayerPieceValue = 1;// white = 1, black = 2
	private boolean isMultiplayer = false;
	private boolean isHard = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Othello();
					window.gameFrame.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Othello() {
		initializeChoiceFrames();
		initializeGameFrame();
		initializeGrid();
		readInternalLogic();
	}

	private void initializeChoiceFrames() {
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception e) {
//		}
		choiceFrame = new JFrame();
		choiceFrame.setBounds(100, 100, 500, 350);
		choiceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		choiceFrame.setResizable(false);
		choiceFrame.setTitle("Othello");
		choiceFrame.getContentPane().setLayout(null);
		choiceFrame.setVisible(true);

		JButton singlePlayerButton = new JButton("One Player");
		singlePlayerButton.setFont(new Font("Arial", Font.PLAIN, 20));
		singlePlayerButton.setBounds(80, 90, 150, 150);
		singlePlayerButton.setFocusPainted(false);
		singlePlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isMultiplayer = false;
				window.choiceFrame.setVisible(false);

				difficultyFrame = new JFrame();
				difficultyFrame.setBounds(100, 100, 500, 350);
				difficultyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				difficultyFrame.setResizable(false);
				difficultyFrame.setTitle("Othello");
				difficultyFrame.getContentPane().setLayout(null);
				difficultyFrame.setVisible(true);

				JButton easyButton = new JButton("Easy");
				easyButton.setFont(new Font("Arial", Font.PLAIN, 20));
				easyButton.setBounds(80, 90, 150, 150);
				easyButton.setFocusPainted(false);
				easyButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						isHard = false;
						window.difficultyFrame.setVisible(false);
						window.gameFrame.setVisible(true);
					}
				});

				JButton hardButton = new JButton("Hard");
				hardButton.setFont(new Font("Arial", Font.PLAIN, 20));
				hardButton.setFocusPainted(false);
				hardButton.setBounds(270, 90, 150, 150);
				hardButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						isHard = true;
						window.difficultyFrame.setVisible(false);
						window.gameFrame.setVisible(true);
					}
				});

				difficultyFrame.getContentPane().add(easyButton);
				difficultyFrame.getContentPane().add(hardButton);
			}
		});
		choiceFrame.getContentPane().add(singlePlayerButton);

		////////////////////////////////////////////////////

		JButton twoPlayerButton = new JButton("Two Players");
		twoPlayerButton.setFont(new Font("Arial", Font.PLAIN, 20));
		twoPlayerButton.setBounds(270, 90, 150, 150);
		twoPlayerButton.setFocusPainted(false);
		twoPlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isMultiplayer = true;
				window.choiceFrame.setVisible(false);
				window.gameFrame.setVisible(true);
			}
		});

		choiceFrame.getContentPane().add(twoPlayerButton);

	}

	/**
	 * Initialize the contents of the gameFrame.
	 */
	private void initializeGameFrame() {
		gameFrame = new JFrame();
		gameFrame.setBounds(100, 100, 639, 727);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setResizable(false);
		gameFrame.setTitle("Othello");
		gameFrame.setBackground(Color.BLACK);
		gameFrame.getContentPane().setLayout(null);

		
		
		JButton newGameButton = new JButton("New Game");
		newGameButton.setBounds(268, 664, 105, 23);
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				internalLogic = new InternalLogic();
				readInternalLogic();
			}
		});
		gameFrame.getContentPane().add(newGameButton);

		internalLogic = new InternalLogic();

	}

	private void initializeGrid() {
		gridOfSquares = new JButton[8][8];

		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				gridOfSquares[row][column] = new JButton();
				gridOfSquares[row][column].setIcon(new ImageIcon(Othello.class.getResource(GREEN_SQUARE_IMAGE_PATH)));
				gridOfSquares[row][column].setBounds(79 * column, 80 * row, 80, 80);
				gridOfSquares[row][column].setContentAreaFilled(false);
				gridOfSquares[row][column].setRolloverEnabled(false);
				gridOfSquares[row][column].setBorderPainted(false);
				

				final int r = row;
				final int c = column;
				gridOfSquares[row][column].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						readInternalLogic();

						ArrayList<Move> possibleMoves = internalLogic.getPossibleMoves(currentPlayerPieceValue);
						for (Move move : possibleMoves) {
							if (move.getRow() == r && move.getColumn() == c) {
								internalLogic.makeMove(r, c, currentPlayerPieceValue);
								readInternalLogic();

								if (isMultiplayer) {
									if (currentPlayerPieceValue == 1) {
										currentPlayerPieceValue++;
										JOptionPane.showMessageDialog(null, "Black's Move");

									} else {
										currentPlayerPieceValue--;
										JOptionPane.showMessageDialog(null, "White's Move");

									}
								} else {
									JOptionPane.showMessageDialog(null, "Opponent Move");
									if (isHard) {
										internalLogic.doMaxPieceGainOpponentMove();
									} else {
										internalLogic.doNaiveOpponentMove();
									}
								}

								break;
							}
						}
						if (possibleMoves.size() == 0) {
							JOptionPane.showMessageDialog(null, internalLogic.getWinner());
						}

						readInternalLogic();

					}
				});

				gameFrame.getContentPane().add(gridOfSquares[row][column]);

			}
		}

	}

	private void readInternalLogic() {
		int[][] grid = internalLogic.getGrid();

		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				String imagePath = "";
				switch (grid[r][c]) {
				case 2:
					imagePath = BLACK_PIECE_IMAGE_PATH;
					break;
				case 1:
					imagePath = WHITE_PIECE_IMAGE_PATH;
					break;
				default:
					imagePath = GREEN_SQUARE_IMAGE_PATH;
				}
				gridOfSquares[r][c].setIcon((new ImageIcon(Othello.class.getResource(imagePath))));
			}
		}

		ArrayList<Move> possibleMoves = internalLogic.getPossibleMoves(currentPlayerPieceValue);
		for (Move move : possibleMoves) {
//			System.out.println(move.getRow());
//			System.out.println(move.getColumn() + "\n" + "\n");
			gridOfSquares[move.getRow()][move.getColumn()]
					.setIcon((new ImageIcon(Othello.class.getResource(POSSIBLE_MOVE_IMAGE_PATH))));
		}
	}
}
