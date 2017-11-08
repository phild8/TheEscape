package edu.orangecoastcollege.escapethecatcher;

/**
 * Creates a zombie object that can move across a game board.
 */
public class Zombie {

    private int mRow;
    private int mCol;

    /**
     * Allows the zombie object to move up, down, left, or right inside of a 2d array which represents
     * a game board.
     * @param gameBoard The 2d array that the zombie travels on.
     * @param playerCol The zombies column
     * @param playerRow The zombies row
     */
    public void move(int[][] gameBoard, int playerCol, int playerRow) {
        if (mCol < playerCol && gameBoard[mRow][mCol + 1] == BoardCodes.EMPTY) {
            mCol++;
        } else if (mCol > playerCol && gameBoard[mRow][mCol - 1] == BoardCodes.EMPTY) {
            mCol--;
        } else if (mRow < playerRow && gameBoard[mRow + 1][mCol] == BoardCodes.EMPTY) {
            mRow++;
        } else if (mRow > playerRow && gameBoard[mRow - 1][mCol] == BoardCodes.EMPTY) {
            mRow--;
        }
    }

    /**
     * Sets a new row for the zombie.
     * @param row The new row.
     */
    public void setRow(int row) {
        mRow = row;
    }

    /**
     * Gets the row that zombie is on.
     * @return The row.
     */
    public int getRow() {
        return mRow;
    }

    /**
     * The column that zombie is on.
     * @param col The new col.
     */
    public void setCol(int col) {
        mCol = col;
    }

    /**
     * Gets the col that zombie is on.
     * @return The col.
     */
    public int getCol() {
        return mCol;
    }

}
