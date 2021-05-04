package swe4.slidingpuzzle;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import swe4.astar.AStarSolver;
import swe4.astar.NoSolutionException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SlidingPuzzleTest {

    @Test
    public void solveSimplePuzzle1() {
        try {
            AStarSolver solver = new AStarSolver();

            Board board = new Board(3);
            board.setTile(1, 1, 1);
            board.setTile(1, 2, 2);
            board.setTile(1, 3, 3);
            board.setTile(2, 1, 4);
            board.setTile(2, 2, 5);
            board.setTile(2, 3, 6);
            board.setTile(3, 1, 7);
            board.setTile(3, 2, 0);
            board.setTile(3, 3, 8);

            List<Move> moves = (List<Move>) solver.solve(board);
            assertEquals(1, moves.size());

            Move move = moves.get(0);
            assertTrue(move.getRow() == 3 && move.getColumn() == 3);
        } catch (NoSolutionException nse) {
            fail("NoSolutionException is not expected.");
        }
    }

    @Test
    public void solveSimplePuzzle2() {
        try {
            AStarSolver solver = new AStarSolver();

            Board board = new Board(3);
            board.setTile(1, 1, 1);
            board.setTile(1, 2, 2);
            board.setTile(1, 3, 3);
            board.setTile(2, 1, 4);
            board.setTile(2, 2, 5);
            board.setTile(2, 3, 6);
            board.setTile(3, 1, 0);
            board.setTile(3, 2, 7);
            board.setTile(3, 3, 8);

            List<Move> moves = (List<Move>) solver.solve(board);
            assertEquals(2, moves.size());

            Move move1 = moves.get(0);
            Move move2 = moves.get(1);
            assertTrue(move1.getRow() == 3 && move1.getColumn() == 2);
            assertTrue(move2.getColumn() == 3 && move2.getRow() == 3);
        } catch (NoSolutionException nse) {
            fail("NoSolutionException is not expected.");
        }
    }

    @Test
    public void solveComplexPuzzle1() {

        try {
            AStarSolver solver = new AStarSolver();

            // 8  2  7
            // 1  4  6
            // 3  5  X
            Board board = new Board(3);
            board.setTile(1, 1, 8);
            board.setTile(1, 2, 2);
            board.setTile(1, 3, 7);
            board.setTile(2, 1, 1);
            board.setTile(2, 2, 4);
            board.setTile(2, 3, 6);
            board.setTile(3, 1, 3);
            board.setTile(3, 2, 5);
            board.setTile(3, 3, 0);

            List<Move> moves = (List<Move>) solver.solve(board);

            for (Move move : moves)
                board = board.apply(move);

            assertEquals(new Board(3), board);
        } catch (NoSolutionException nse) {
            fail("NoSolutionException is not expected.");
        }
    }

    @Test
    public void solveRandomPuzzles() {
        AStarSolver solver = new AStarSolver();

        for (int k = 0; k < 50; k++) {
            try {
                Board board = new Board(3);
                int n = 1;
                int maxN = board.size() * board.size();
                for (int i = 1; i <= board.size(); i++)
                    for (int j = 1; j <= board.size(); j++)
                        board.setTile(i, j, (n++) % maxN);

                board.shuffle();

                List<Move> moves = (List<Move>) solver.solve(board);

                for (Move move : moves)
                    board = board.apply(move);
                Board expected = new Board(3);
                assertEquals(expected, board);
            } catch (NoSolutionException nse) {
                fail("NoSolutionException is not expected.");
            }
        }
    }

    @Test
    public void solveSimplePuzzle_4x4() {
        try {
            AStarSolver solver = new AStarSolver();
            Board board = new Board(4);

            board.moveLeft();

            List<Move> moves = (List<Move>) solver.solve(board);
            assertEquals(1, moves.size());

            Move move = moves.get(0);
            assertTrue(move.getRow() == 4 && move.getColumn() == 4);
        } catch (NoSolutionException nse) {
            fail("NoSolutionException is not expected.");
        }
    }

    @Test
    public void solveComplexPuzzle_4x4() {
        try {
            AStarSolver solver = new AStarSolver();

            Board board = new Board(4);
            board.moveLeft();
            board.moveLeft();
            board.moveUp();
            board.moveLeft();
            board.moveUp();
            board.moveUp();
            board.moveRight();
            board.moveDown();
            board.moveLeft();

            List<Move> moves = (List<Move>) solver.solve(board);

            for (Move move : moves)
                board = board.apply(move);

            assertEquals(new Board(4), board);
        } catch (NoSolutionException nse) {
            fail("NoSolutionException is not expected.");
        }
    }

    @Test
    //@Disabled
    public void solveRandomPuzzlesTest_4x4() {
        AStarSolver solver = new AStarSolver();

        for (int k = 0; k < 10; k++) {
            try {
                Board board = new Board(4);
                int n = 1;
                int maxN = board.size() * board.size();
                for (int i = 1; i <= board.size(); i++)
                    for (int j = 1; j <= board.size(); j++)
                        board.setTile(i, j, (n++) % maxN);

                board.shuffle();

                List<Move> moves = (List<Move>) solver.solve(board);

                for (Move move : moves)
                    board = board.apply(move);

                assertEquals(new Board(4), board);
            } catch (NoSolutionException nse) {
                fail("NoSolutionException is not expected.");
            }
        }
    }

    @DisplayName("Board.move when invalid position throws IllegalMoveException")
    @Test
    void moveWhenInvalidPositionThrowsIllegalMoveException() {
        //given
        Board board = new Board(3);

        //when
        //then
        assertThrows(IllegalMoveException.class, () -> board.move(1, 1));
    }

    @DisplayName("Board.setTile when greater than max number set throws InvalidTileNumberException")
    @Test
    void setTIleWhenGreaterThanMaxNumberSetThrowsInvalidTileNumberException() {
        //given
        Board board = new Board(3);
        board.setEmptyTile(3, 3);

        //when
        //then
        assertThrows(InvalidTileNumberException.class, () -> board.setTile(2, 2, 3 * 3));
    }

    @DisplayName("Board.compareTo returns < 0 if other board is greater")
    @Test
    void compareToReturnsLessThanZeroIfOtherBoardIsGreater() {
        //given
        Board board = new Board(3);
        Board otherBoard = new Board(4);

        //when
        int result = board.compareTo(otherBoard);

        //then
        assertTrue(result < 0);
    }

    @DisplayName("Board.compareTo returns > 0 if other board is smaller")
    @Test
    void compareToReturnsGreaterThanZeroIfOtherBoardIsSmaller() {
        //given
        Board board = new Board(3);
        Board otherBoard = new Board(2);

        //when
        int result = board.compareTo(otherBoard);

        //then
        assertTrue(result > 0);
    }

    @DisplayName("Board.compareTo returns 0 if boards have the same size")
    @Test
    void compareToReturnsZeroIfBoardsHaveTheSameSize() {
        //given
        Board board = new Board(3);
        Board otherBoard = new Board(3);

        //when
        int result = board.compareTo(otherBoard);

        //then
        assertEquals(0, result);
    }

    @DisplayName("Board.getTile when invalid indices throws InvalidBoardIndexException")
    @Test
    void getTileWhenInvalidIndicesThrowsInvalidBoardIndexException() {
        //given
        Board board = new Board(3);

        //when
        //then
        assertThrows(InvalidBoardIndexException.class, () -> board.getTile(-1, 2));
    }

    @DisplayName("Board empty tile is on the lower right")
    @Test
    void emptyTileIsOnTheLowerRight() {
        //given
        //when
        Board board = new Board(3);

        //then
        assertTrue(board.getEmptyTileColumn() == 3 && board.getEmptyTileRow() == 3);
    }

    @DisplayName("Board.toString simple test")
    @Test
    void toStringSimpleTest() {
        //given
        Board board = new Board(3);

        //when
        String str = board.toString();
        //then
        for (int i = 1; i < 3 * 3; i++) {
            assertTrue(str.contains(Integer.toString(i)));
        }
        assertTrue(str.contains("X"));
    }
}
