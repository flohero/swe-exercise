package swe4.slidingpuzzle;

import swe4.astar.Transition;

public class Move implements Transition {

    private static final double MOVE_COSTS = 1.0;

    public final int row;
    public final int column;

    public Move(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public double costs() {
        return MOVE_COSTS;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
