package edu.orangecoastcollege.escapethecatcher;

public class Player {
    private int mRow;
    private int mCol;

    /**
     * Moves the player object up, down, left, or right inside of a 2d array which represents a GUI.
     * @param gameBoard The game board the player object travels on.
     * @param direction The direction the player is traveling on, which is determined on he fling.
     */
    public void move(int[][] gameBoard, String direction) {

        // COMPLETED: Implement the logic for the move operation
        // COMPLETED: If the gameBoard is obstacle free in the direction requested,
        // COMPLETED: Move the player in the intended direction.  Otherwise, do nothing (player loses turn)
        if (direction.equals("UP") && gameBoard[mRow-1][mCol] == 2)
            mRow--;
        else if (direction.equals("DOWN") && gameBoard[mRow+1][mCol] == 2)
            mRow++;
        else if (direction.equals("LEFT") && gameBoard[mRow][mCol-1] == 2)
            mCol--;
        else if (direction.equals("RIGHT") && gameBoard[mRow][mCol+1] == 2){
            mCol++;
        }
    }

    /**
     * sets the row the player object is on.
     * @param row The new row.
     */
    public void setRow(int row) {
        mRow = row;
    }

    /**
     * Gets the row that the player object is on.
     * @return The row.
     */
    public int getRow() {
        return mRow;
    }

    /**
     * Sets the column the player object is on.
     * @param col The new column.
     */
    public void setCol(int col) {
        mCol = col;
    }

    /**
     * Gets the column that the player object is on.
     * @return The col.
     */
    public int getCol() {
        return mCol;
    }

}
