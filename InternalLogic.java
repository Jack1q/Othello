import java.util.ArrayList;

import javax.swing.JOptionPane;

public class InternalLogic {
	private int[][] grid;

	private final int EMPTY_SQUARE_VALUE = 0;
	private final int WHITE_PIECE_VALUE = 1;
	private final int BLACK_PIECE_VALUE = 2;

	public InternalLogic() {
		grid = new int[8][8];
		setGridDefaults();
	}

	public void setGridDefaults() {
		// 0 = empty
		// 1 = white
		// 2 = black
		int[][] gridDefaults = { { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 2, 1, 0, 0, 0 }, { 0, 0, 0, 1, 2, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, };
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				grid[r][c] = gridDefaults[r][c];
			}
		}
	}

	private void copy2DArray(int[][] grid, int[][] temp) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				temp[i][j] = grid[i][j];
			}
		}
	}

	public void makeMove(int row, int column, int pieceValue) {
		grid[row][column] = pieceValue;

		// now copy array
		int[][] temp = new int[8][8];
		copy2DArray(grid, temp);

		int opponentPiece = getOpponentPieceValue(pieceValue);
		int i;
		int j;

		// Look for a white piece BELOW the square selected.

		i = row + 1;
		while (i < 8 && grid[i][column] == opponentPiece) {
			i++;
		}
		if (i < 8 && grid[i][column] == pieceValue) {
			// make all pieces in between = to piece value
			for (int k = row; k < i; k++) {
				temp[k][column] = pieceValue;
			}
		}

		// check if there's one LEFT

		j = column - 1;
		while (j > 0 && grid[row][j] == opponentPiece) {
			j--;
		}
		if (j >= 0 && grid[row][j] == pieceValue) {
			for (int k = j; k < column; k++) {
				temp[row][k] = pieceValue;
			}
		}

		// below left
		i = row + 1;
		j = column - 1;
		while (i < 8 && j > 0 && grid[i][j] == opponentPiece) {
			i++;
			j--;
		}
		if (i < 8 && j >= 0 && grid[i][j] == pieceValue) {
			int nextRow = i;
			int nextCol = j;

			while (nextRow > row && nextCol < column) {
				temp[nextRow][nextCol] = pieceValue;
				nextRow--;
				nextCol++;
			}
		}

		// RIGHT
		j = column + 1;
		while (j < 8 && grid[row][j] == opponentPiece) {
			j++;
		}
		if (j < 8 && grid[row][j] == pieceValue) {
			for (int k = column; k < j; k++) {
				temp[row][k] = pieceValue;
			}
		}

		// BELOW RIGHT
		i = row + 1;
		j = column + 1;
		while (i < 7 && j < 8 && grid[i][j] == opponentPiece) {
			i++;
			j++;
		}
		if (i < 8 && j < 8 && grid[i][j] == pieceValue) {
			int k = row;
			int l = column;
			while (k < i && l < j) {
				temp[k][l] = pieceValue;
				k++;
				l++;
			}
		}

		// check if there's one LEFT

		j = column - 1;
		while (j > 0 && grid[row][j] == opponentPiece) {
			j--;
		}
		if (j >= 0 && grid[row][j] == pieceValue) {
			for (int k = j; k < column; k++) {
				temp[row][k] = pieceValue;
			}
		}

		// check if theres one ABOVE - WORKS

		i = row - 1;
		while (i > 0 && grid[i][column] == opponentPiece) {
			i--;
		}

		if (i >= 0 && grid[i][column] == pieceValue) {
			for (int k = i; k < row; k++) {
				temp[k][column] = pieceValue;
			}
		}

		// check ABOVE-LEFT

		i = row - 1;
		j = column - 1;
		while (i > 0 && j > 0 && grid[i][j] == opponentPiece) {
			i--;
			j--;
		}
		if (i >= 0 && j >= 0 && grid[i][j] == pieceValue) {
			int k = i;
			int l = j;
			while (k < row && l < column) {
				temp[k][l] = pieceValue;
				k++;
				l++;
			}
		}

		// check ABOVE- RIGHT
		i = row - 1;
		j = column + 1;
		while (i > 0 && j < 8 && grid[i][j] == opponentPiece) { // perhaps j < 7 ?
			i--;
			j++;
		}
		if (i >= 0 && j < 8 && grid[i][j] == pieceValue) {

			int k = i;
			int l = j;

			while (k < row && l > column) {
				temp[k][l] = pieceValue;
				k++;
				l--;
			}

		}

		copy2DArray(temp, grid);

	}

	public ArrayList<Move> getPossibleMoves(int playerPieceValue) {
		ArrayList<Move> moves = new ArrayList<>();

		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (grid[r][c] == playerPieceValue) {
					ArrayList<Move> pieceMoves = getPossibleMovesForGivenPiece(new Move(r, c), playerPieceValue);

					// add pieceMoves to moves.
					for (Move move : pieceMoves) {
						moves.add(move);
					}
				}
			}
		}

		return moves;
	}

	public ArrayList<Move> getPossibleMovesForGivenPiece(Move pieceLocation, int pieceValue) {
		ArrayList<Move> moves = new ArrayList<>();
		int row = pieceLocation.getRow();
		int column = pieceLocation.getColumn();
		int opponentPiece = getOpponentPieceValue(pieceValue);

		int i;
		int j;
		int count;

		// LOOK UP
		i = row - 1;
		count = 0;
		if (i >= 0 && grid[i][column] == opponentPiece) {
			while (i > 0 && grid[i][column] == opponentPiece) {
				i--;
				count++;
			}

			if (i >= 0 && grid[i][column] == EMPTY_SQUARE_VALUE)
				moves.add(new Move(i, column, count));
		}

		// LOOK DOWN
		i = row + 1;
		count = 0;
		if (i < 8 && grid[i][column] == opponentPiece) {
			while (i < 8 && grid[i][column] == opponentPiece) {
				i++;
				count++;
			}

			if (i < 8 && grid[i][column] == EMPTY_SQUARE_VALUE)
				moves.add(new Move(i, column, count));
		}

		// LOOK LEFT
		j = column - 1;
		count = 0;
		if (j > 0 && grid[row][j] == opponentPiece) {
			while (j > 0 && grid[row][j] == opponentPiece) {
				j--;
				count++;
			}
			if (j >= 0 && grid[row][j] == EMPTY_SQUARE_VALUE)
				moves.add(new Move(row, j, count));
		}

		// LOOK RIGHT
		j = column + 1;
		count = 0;
		if (j < 8 && grid[row][j] == opponentPiece) {
			while (j < 8 && grid[row][j] == opponentPiece) {
				j++;
				count++;
			}
			if (j < 8 && grid[row][j] == EMPTY_SQUARE_VALUE)
				moves.add(new Move(row, j, count));
		}

		// LOOK UP LEFT
		i = row - 1;
		j = column - 1;
		count = 0;
		if (i >= 0 && j >= 0 && grid[i][j] == opponentPiece) {
			while (j > 0 && i > 0 && grid[i][j] == opponentPiece) {
				j--;
				i--;
				count++;
			}
			if (j >= 0 && i >= 0 && grid[i][j] == EMPTY_SQUARE_VALUE)
				moves.add(new Move(i, j, count));
		}

		// LOOK UP RIGHT
		i = row - 1;
		j = column + 1;
		count = 0;
		if (i >= 0 && j < 8 && grid[i][j] == opponentPiece) {
			while (j < 8 && i > 0 && grid[i][j] == opponentPiece) {
				i--;
				j++;
				count++;
			}
			if (i >= 0 && j < 8 && grid[i][j] == EMPTY_SQUARE_VALUE)
				moves.add(new Move(i, j, count));
		}

		// DOWN LEFT
		i = row + 1;
		j = column - 1;
		count = 0;
		if (i < 8 && j >= 0 && grid[i][j] == opponentPiece) {
			while (j > 0 && i < 8 && grid[i][j] == opponentPiece) {
				i++;
				j--;
				count++;
			}
			if (i < 8 && j >= 0 && grid[i][j] == EMPTY_SQUARE_VALUE)
				moves.add(new Move(i, j, count));
		}

		// LOOK DOWN RIGHT
		i = row + 1;
		j = column + 1;
		count = 0;
		if (i < 8 && j < 8 && grid[i][j] == opponentPiece) {
			while (j < 8 && i < 8 && grid[i][j] == opponentPiece) {
				j++;
				i++;
				count++;
			}
			if (i < 8 && j < 8 && grid[i][j] == EMPTY_SQUARE_VALUE)
				moves.add(new Move(i, j, count));
		}

		return moves;
	}

	public int[][] getGrid() {
		return grid;
	}

	public int getOpponentPieceValue(int pieceValue) {
		if (pieceValue == WHITE_PIECE_VALUE)
			return BLACK_PIECE_VALUE;
		return WHITE_PIECE_VALUE;
	}

	public void doNaiveOpponentMove() {
		ArrayList<Move> moves = getPossibleMoves(2);
		try {
			Move randomMove = moves.get((int) (Math.random() * moves.size()));
			makeMove(randomMove.getRow(), randomMove.getColumn(), BLACK_PIECE_VALUE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, getWinner());
		}
	}

	public void doMaxPieceGainOpponentMove() {
		ArrayList<Move> moves = getPossibleMoves(2);
		try {
			Move moveWithMaxWeight = moves.get(0);
			for (Move move : moves) {
				if (move.getMoveWeight() > moveWithMaxWeight.getMoveWeight()) {
					moveWithMaxWeight = move;
				}
			}
			makeMove(moveWithMaxWeight.getRow(), moveWithMaxWeight.getColumn(), BLACK_PIECE_VALUE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, getWinner());
		}
	}

	public String getWinner() {
		int white = 0;
		int black = 0;
		for (int row[] : grid) {
			for (int piece : row) {
				if (piece == WHITE_PIECE_VALUE) {
					white++;
				} else if (piece == BLACK_PIECE_VALUE) {
					black++;
				}
			}
		}
		if (white > black)
			return "White wins";
		else if (black > white)
			return "Black wins";
		return "Tie";

	}

}
