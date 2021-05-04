package swe4.slidingpuzzle;

import org.junit.jupiter.api.Test;
import swe4.astar.MathUtil;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class BoardTest {

  @Test
  public void boardValid1() {
    Board board;
    try {
      // 1 2 3
      // 4 5 6
      // 7 8 _
      board = new Board(3);
      board.setTile(1, 1, 1);
      board.setTile(1, 2, 2);
      board.setTile(1, 3, 3);
      board.setTile(2, 1, 4);
      board.setTile(2, 2, 5);
      board.setTile(2, 3, 6);
      board.setTile(3, 1, 7);
      board.setTile(3, 2, 8);
      board.setTile(3, 3, 0);

      assertTrue(board.isValid());
    }
    catch (BoardException e) {
      fail("BoardException not expected.");
    }
  }

  @Test
  public void boardValid2() {
    Board board;
    try {
      // 1 2 3
      // 4 5 6
      // 7 1 _
      board = new Board(3);
      board.setTile(1, 1, 1);
      board.setTile(1, 2, 2);
      board.setTile(1, 3, 3);
      board.setTile(2, 1, 4);
      board.setTile(2, 2, 5);
      board.setTile(2, 3, 6);
      board.setTile(3, 1, 7);
      board.setTile(3, 2, 1);
      board.setTile(3, 3, 0);

      assertTrue(!board.isValid());
    }
    catch (BoardException e) {
      fail("BoardException not expected.");
    }
  }

  @Test
  public void boardValid3() {
    Board board;
    try {
      // 8 2 _
      // 7 5 4
      // 3 1 6
      board = new Board(3);
      board.setTile(1, 1, 8);
      board.setTile(1, 2, 2);
      board.setTile(1, 3, 0);
      board.setTile(2, 1, 7);
      board.setTile(2, 2, 5);
      board.setTile(2, 3, 4);
      board.setTile(3, 1, 3);
      board.setTile(3, 2, 1);
      board.setTile(3, 3, 6);

      assertTrue(board.isValid());
    }
    catch (BoardException e) {
      fail("BoardException not expected.");
    }
  }

  @Test
  public void estimatedCostsFromTargetToTarget() {
    try {
      Board board = new Board(3);
      board.setTile(1, 1, 1);
      board.setTile(1, 2, 2);
      board.setTile(1, 3, 3);
      board.setTile(2, 1, 4);
      board.setTile(2, 2, 5);
      board.setTile(2, 3, 6);
      board.setTile(3, 1, 7);
      board.setTile(3, 2, 8);
      board.setTile(3, 3, 0);
      assertTrue(MathUtil.isDoubleEqual(0, board.estimatedCostsToTarget()));
    }
    catch (BoardException e) {
      fail("Unexpeced BoardException.");
    }
  }

  @Test
  public void estimatedCosts1() {
    try {
      Board board = new Board(3);
      board = new Board(3);
      board.setTile(1, 1, 1);
      board.setTile(1, 2, 2);
      board.setTile(1, 3, 3);
      board.setTile(2, 1, 4);
      board.setTile(2, 2, 0);
      board.setTile(2, 3, 6);
      board.setTile(3, 1, 7);
      board.setTile(3, 2, 8);
      board.setTile(3, 3, 5);
      assertTrue(MathUtil.isDoubleEqual(2, board.estimatedCostsToTarget()));
    }
    catch (BoardException e) {
      fail("Unexpeced BoardException.");
    }
  }

  @Test
  public void estimatedCosts2() {
    try {
      Board board = new Board(3);
      board = new Board(3);
      board.setTile(1, 1, 1);
      board.setTile(1, 2, 0);
      board.setTile(1, 3, 3);
      board.setTile(2, 1, 4);
      board.setTile(2, 2, 5);
      board.setTile(2, 3, 6);
      board.setTile(3, 1, 7);
      board.setTile(3, 2, 8);
      board.setTile(3, 3, 2);
      assertTrue(MathUtil.isDoubleEqual(3, board.estimatedCostsToTarget()));
    }
    catch (BoardException e) {
      fail("Unexpeced BoardException.");
    }
  }
}
