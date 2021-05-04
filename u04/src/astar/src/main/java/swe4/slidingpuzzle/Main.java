package swe4.slidingpuzzle;

import swe4.astar.AStarSolver;
import swe4.astar.Transition;

import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        basicBoard();
        complexBoard();
    }

    private static void printTransitions(List<? extends Transition> transitions) {
        System.out.println("Minimal Transitions: " + transitions.size());
        String out = transitions.stream().map(transition -> {
            Move move = (Move) transition;
            return "(" + move.getRow() + ", " + move.getColumn() + ")";
        }).collect(Collectors.joining("->", "", "\n"));
        System.out.println(out);
    }

    private static void basicBoard() {
        try {
            Board board = new Board(3);
            board.setTile(1, 1, 1);
            board.setTile(1, 2, 5);
            board.setTile(1, 3, 4);

            board.setTile(2, 1, 8);
            board.setTile(2, 2, 7);
            board.setTile(2, 3, 2);

            board.setTile(3, 1, 0);
            board.setTile(3, 2, 3);
            board.setTile(3, 3, 6);

            System.out.println("--- Simple Board ---\n" + board);
            System.out.println("estimated Costs To Target: " + board.estimatedCostsToTarget());

            AStarSolver solver = new AStarSolver();
            printTransitions(solver.solve(board));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void complexBoard() {
        try {
            Board board = new Board(4);
            board.setTile(1, 1, 3);
            board.setTile(1, 2, 1);
            board.setTile(1, 3, 11);
            board.setTile(1, 4, 6);

            board.setTile(2, 1, 2);
            board.setTile(2, 2, 7);
            board.setTile(2, 3, 0);
            board.setTile(2, 4, 4);

            board.setTile(3, 1, 9);
            board.setTile(3, 2, 10);
            board.setTile(3, 3, 5);
            board.setTile(3, 4, 13);

            board.setTile(4, 1, 14);
            board.setTile(4, 2, 15);
            board.setTile(4, 3, 12);
            board.setTile(4, 4, 8);

            System.out.println("--- Complex Board ---\n" + board);
            System.out.println("estimated Costs To Target: " + board.estimatedCostsToTarget());

            AStarSolver solver = new AStarSolver();
            printTransitions(solver.solve(board));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}