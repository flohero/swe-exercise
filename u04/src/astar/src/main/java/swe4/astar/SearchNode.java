package swe4.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// SearchNode ist eine Hilfsklasse, die zur Implementierung des A*-Algorithmus
// benötigt wird. Damit kann man den Weg von einem SearchNode zum Startknoten
// zurückverfolgen, da dieser mit seinem Vorgängerkonten verkettet ist. Ein
// SearchNode kennt die Kosten vom Startknoten bis zu ihm selbst. SearchNode
// kann auch eine Schätzung für den Weg zum Zielknoten berechnen.
public class SearchNode implements Comparable<SearchNode> {

    private final State state;
    private SearchNode predecessor = null;
    private Transition transition = null;
    private double costsFromStart = 0;

    public SearchNode(State state) {
        this.state = state;
    }

    public SearchNode(State state, SearchNode predecessor, Transition transition) {
        this.state = state;
        this.predecessor = predecessor;
        this.transition = transition;
        calculateCostsFromStart();
    }

    public State getState() {
        return this.state;
    }

    public SearchNode getPredecessor() {
        return this.predecessor;
    }

    /**
     * Setter shouldn't return something.
     * Will not be changed
     *
     * @return the parameter
     */
    public SearchNode setPredecessor(SearchNode predecessor) {
        this.predecessor = predecessor;
        calculateCostsFromStart();
        return this.predecessor;
    }

    public Transition getTransition() {
        return transition;
    }

    public void setTransition(Transition transition) {
        this.transition = transition;
    }

    /**
     * Calculates the number of predecessors
     *
     * @return the costs from the start node to this one
     */
    public double costsFromStart() {
        return this.costsFromStart;
    }

    private void calculateCostsFromStart() {
        costsFromStart = (transition != null ? transition.costs() : 0)
                + (predecessor != null ? predecessor.costsFromStart() : 0);
    }

    /**
     * Set the calculated costs
     *
     * @param costsFromStart
     */
    public void setCostsFromStart(double costsFromStart) {
        this.costsFromStart = costsFromStart;
    }

    public double estimatedCostsToTarget() {
        return this.state.estimatedCostsToTarget();
    }

    public double estimatedTotalCosts() {
        return this.costsFromStart() + this.estimatedCostsToTarget();
    }

    /**
     * Two SearchNodes are equal when their states are equal
     *
     * @param obj other SearchNode
     * @return if the SearchNodes are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SearchNode)) {
            return false;
        }
        SearchNode other = (SearchNode) obj;
        return Objects.equals(this.state, other.state);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(this.state);
    }

    /**
     * Compares the costs of the SearchNodes
     *
     * @return < 0 if costs are greater, >0 if costs are less and 0 if they are the same
     */
    @Override
    public int compareTo(SearchNode other) {
        return (int) (this.costsFromStart() - other.costsFromStart());
    }

    public List<? extends Transition> getTransitionsFromStart() {
        List<Transition> transitions = new ArrayList<>((int) Math.ceil(this.costsFromStart()));
        for (SearchNode node = this; node != null; node = node.predecessor) {
           if(node.transition != null) {
               transitions.add(node.transition);
           }
        }
        Collections.reverse(transitions);
        return transitions;
    }
}
