public class Move {
	private int row;
	private int column;
	private int moveWeight;

	public Move(int row, int column) {
		this.row = row;
		this.column = column;
		moveWeight = 0;
	}

	public Move(int row, int column, int moveWeight) {
		this.row = row;
		this.column = column;
		this.moveWeight = moveWeight;
	}

	/**
	 * @return the Move object's position on the y-axis of the board
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return the Move object's position on the x-axis of the board
	 */
	public int getColumn() {
		return column;
	}

	public int getMoveWeight() {
		return moveWeight;
	}

}