package swe4.astar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AStarSolver {


    public List<? extends Transition> solve(State initialState) throws NoSolutionException {
        SearchNode startNode = new SearchNode(initialState);

        // all nodes which need to be visited
        PriorityQueue<SearchNode> openSet = new PriorityQueue<>();
        openSet.add(startNode);

        Map<State, SearchNode> distances = new HashMap<>();

        while (!openSet.isEmpty()) {
            SearchNode current = openSet.remove();

            // Target found
            if (MathUtil.isDoubleZero(current.estimatedCostsToTarget())) {
                return current.getTransitionsFromStart();
            }

            for (Transition transition : current.getState().transitions()) {
                SearchNode neighbour = new SearchNode(current.getState().apply(transition));

                neighbour.setCostsFromStart(Double.POSITIVE_INFINITY);
                neighbour = distances.getOrDefault(neighbour.getState(), neighbour);

                double tentativeG = current.costsFromStart() + transition.costs();
                if (tentativeG < neighbour.costsFromStart()) {
                    // Runs way slower, but does not need as much ram
                    //openSet.remove(neighbour);

                    neighbour.setPredecessor(current);
                    neighbour.setTransition(transition);
                    neighbour.setCostsFromStart(tentativeG);
                    distances.put(neighbour.getState(), neighbour);

                    openSet.add(neighbour);
                }
            }
        }

        throw new NoSolutionException();
    }
}
