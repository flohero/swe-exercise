package swe4.slidingpuzzle;

import swe4.astar.State;
import swe4.astar.Transition;

import java.util.*;


public class Board implements State {
    public static final int EMPTY_TILE_VALUE = 0;
    public static final int MAX_SHUFFLE_ITERATIONS = 51;
    public static final int MIN_SHUFFLE_ITERATIONS = 10;

    private final int size;
    private final int[][] board;
    private int emptyTileRow;
    private int emptyTileColumn;

    public Board(int size) {
        this.size = size;
        this.board = new int[this.size][this.size];
        this.emptyTileRow = this.size;
        this.emptyTileColumn = this.size;
        for (int i = 0, n = 1; i < this.size; i++) {
            for (int j = 0; j < this.size; j++, n++) {
                this.board[i][j] = (n % (this.size * this.size));
            }
        }
        this.board[this.size - 1][this.size - 1] = EMPTY_TILE_VALUE;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Board
                && Arrays.deepEquals(this.board, ((Board) other).board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.board);
    }

    /**
     * @return >0 if size greater, <0 if size smaller and 0 if size equals
     */
    @Override
    public int compareTo(State other) {
        return Integer.compare(this.size, ((Board) other).size);
    }

    @Override
    public List<Move> transitions() {
        List<Move> moves = new ArrayList<>();
        final int[] row = {1, -1, 0, 0};
        final int[] col = {0, 0, 1, -1};

        for (int i = 0; i < row.length; i++) {
            int newEmptyTileRow = row[i] + emptyTileRow;
            int newEmptyTileColumn = col[i] + emptyTileColumn;

            if (isValidIndex(newEmptyTileRow, newEmptyTileColumn)) {
                moves.add(new Move(newEmptyTileRow, newEmptyTileColumn));
            }
        }
        return moves;
    }

    @Override
    public double estimatedCostsToTarget() {
        int estimatedCostsToTarget = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int tile = this.board[i][j];
                if (tile != EMPTY_TILE_VALUE) {
                    Move position = new Move(
                            Math.floorDiv(tile - 1, this.size),
                            (tile - 1) % this.size
                    );
                    estimatedCostsToTarget += Math.abs(i - position.getRow())
                            + Math.abs(j - position.getColumn());
                }
            }
        }
        return estimatedCostsToTarget;
    }

    @Override
    public Board apply(Transition transition) {
        Move move = (Move) transition;
        Board copiedBoard = copy();
        copiedBoard.move(move.getRow(), move.getColumn());
        return copiedBoard;
    }

    public int getTile(int i, int j) throws InvalidBoardIndexException {
        ifNotValidIndexThrow(i, j);
        return this.board[normalizeIndex(i)][normalizeIndex(j)];
    }

    /**
     * Sets the value for a tile[i,j].
     * If one wants to set the value to the EMPTY_TILE_VALUE, use the {@link #setEmptyTile(int, int)} method
     *
     * @param i      row
     * @param j      column
     * @param number new value
     * @throws InvalidBoardIndexException if indices out of bounds
     * @throws InvalidTileNumberException if tile number is greater than the max allowed value,
     *                                    or smaller equals the empty tile value
     */
    public void setTile(int i, int j, int number) {
        ifNotValidIndexThrow(i, j);
        if (number > maxNumber()) {
            throw new InvalidTileNumberException();
        }
        if(number == EMPTY_TILE_VALUE) {
            setEmptyTile(i, j);
        }else {
            this.board[normalizeIndex(i)][normalizeIndex(j)] = number;
        }
    }

    /**
     * Set the tile[i,j] to the empty value
     *
     * @param i row
     * @param j column
     * @throws InvalidBoardIndexException if the index is out of bounds
     */
    public void setEmptyTile(int i, int j) throws InvalidBoardIndexException {
        ifNotValidIndexThrow(i, j);
        this.emptyTileRow = i;
        this.emptyTileColumn = j;
        this.board[normalizeIndex(i)][normalizeIndex(j)] = EMPTY_TILE_VALUE;
    }

    public int getEmptyTileRow() {
        return this.emptyTileRow;
    }

    public int getEmptyTileColumn() {
        return this.emptyTileColumn;
    }

    public int size() {
        return this.size;
    }

    public boolean isValid() {
        Set<Integer> isInBoard = new HashSet<>();
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                // Could use the getTile method
                // but this would be way too impractical
                final int tileValue = this.board[i][j];
                if (isInBoard.contains(tileValue) || tileValue > maxNumber()) {
                    return false;
                }
                isInBoard.add(tileValue);
            }
        }
        return true;
    }

    // Macht eine tiefe Kopie des Boards.
    // Vorsicht: Referenztypen m�ssen neu allokiert und anschlie�end deren
    // Inhalt kopiert werden.
    public Board copy() {
        Board copy = new Board(this.size);
        copy.emptyTileRow = this.emptyTileRow;
        copy.emptyTileColumn = this.emptyTileColumn;
        for (int i = 0; i < this.board.length; i++) {
            copy.board[i] = this.board[i].clone();
        }
        return copy;
    }

    public void shuffle() {
        Random random = new Random();
        final int limit = random.nextInt(MAX_SHUFFLE_ITERATIONS - MIN_SHUFFLE_ITERATIONS) + 10;
        for (int i = 0; i < limit; i++) {
            List<Move> transitions = this.transitions();
            int index = random.nextInt(transitions.size());
            Move move = transitions.get(index);
            move(move.getRow(), move.getColumn());
        }
    }

    /**
     * Since it is not specified, I hope that row and column start at 1
     *
     * @param row starting at 1
     * @param col starting at 1
     */
    public void move(int row, int col) {
        int rowDiff = Math.abs(normalizeIndex(emptyTileRow) - normalizeIndex(row));
        int columnDiff = Math.abs(normalizeIndex(emptyTileColumn) - normalizeIndex(col));
        // if rowDiff + columnDiff != 1 means that the empty tile will either be not moved,
        // or the step is too big or it will be moved diagonally.
        if (!isValidIndex(row, col) || rowDiff + columnDiff != 1) {
            throw new IllegalMoveException();
        }
        int tile = getTile(row, col);
        setTile(emptyTileRow, emptyTileColumn, tile);
        setEmptyTile(row, col);
    }

    public void moveLeft() throws IllegalMoveException {
        move(emptyTileRow, emptyTileColumn - 1);
    }

    public void moveRight() throws IllegalMoveException {
        move(emptyTileRow, emptyTileColumn + 1);
    }

    public void moveUp() throws IllegalMoveException {
        move(emptyTileRow - 1, emptyTileColumn);
    }

    public void moveDown() throws IllegalMoveException {
        move(emptyTileRow + 1, emptyTileColumn);
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                final int tile = this.board[i][j];
                str.append((tile == EMPTY_TILE_VALUE ? "X" : ("" + tile)))
                        .append(" ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    private boolean isValidIndex(int i, int j) {
        return (i > 0  && j > 0 && i <= this.size && j <= this.size);
    }

    private void ifNotValidIndexThrow(int i, int j) throws InvalidBoardIndexException {
        if (!isValidIndex(i, j)) {
            throw new InvalidBoardIndexException();
        }
    }

    /**
     * I don't know why I need to have to implement this method,
     * since this is more a library than an user facing interface,
     * and I believe sanitizing and normalizing the input should not be part of this class.
     *
     * @param index
     * @return the index - 1
     */
    private int normalizeIndex(int index) {
        return index - 1;
    }

    private int maxNumber() {
        return (size * size) - 1;
    }
}
