package swe4.astar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import swe4.astar.MathUtil;
import swe4.astar.SearchNode;
import swe4.astar.State;
import swe4.astar.Transition;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SearchNodeTest {

    private static class Vertex implements State {

        private int id;
        private double costs;
        private List<Transition> transitions = new ArrayList<>();

        public Vertex(int id, double costs) {
            this.id = id;
            this.costs = costs;
        }

        @Override
        public List<Transition> transitions() {
            return transitions;
        }

        public void addEdge(Edge edge) {
            transitions.add(edge);
        }

        @Override
        public State apply(Transition transition) {
            return ((Edge) transition).to;
        }

        @Override
        public double estimatedCostsToTarget() {
            return costs;
        }

        @Override
        public int compareTo(State other) {
            return Integer.compare(this.id, ((Vertex) other).id);
        }
    }

    private static class Edge implements Transition {

        private Vertex to;
        private double costs;

        public Edge(Vertex to, double costs) {
            this.to = to;
            this.costs = costs;
        }

        @Override
        public double costs() {
            return costs;
        }
    }

    @Test
    public void oneParamConstructorInitializesCorrectly() {
        //given
        Vertex v = new Vertex(1, 10.0);
        //when
        SearchNode sn = new SearchNode(v);
        //then
        assertNotNull(sn.getState());
        assertNull(sn.getPredecessor());
        assertNull(sn.getTransition());
    }

    @Test
    public void threeParamConstructorInitializesCorrectly() {
        //given
        Vertex v1 = new Vertex(1, 10.0);
        Vertex v2 = new Vertex(2, 10.0);
        Edge e12 = new Edge(v2, 15.0);
        v1.addEdge(e12);
        //when
        SearchNode sn1 = new SearchNode(v1);
        SearchNode sn2 = new SearchNode(v2, sn1, e12);
        //then
        assertSame(v2, sn2.getState());
        assertSame(sn1, sn2.getPredecessor());
        assertSame(e12, sn2.getTransition());
    }

    @Test
    public void constructorInitializesCostsCorrectly() {
        //given
        Vertex v1 = new Vertex(1, 10.0);
        Vertex v2 = new Vertex(2, 10.0);
        Edge e12 = new Edge(v2, 15.0);
        v1.addEdge(e12);

        SearchNode sn1 = new SearchNode(v1);
        SearchNode sn2 = new SearchNode(v2, sn1, e12);
        //when
        //then
        assertTrue(MathUtil
                .isDoubleEqual(v2.estimatedCostsToTarget(), sn2.estimatedCostsToTarget()));
        assertTrue(MathUtil
                .isDoubleEqual(sn1.costsFromStart() + e12.costs(), sn2.costsFromStart()));
    }

    @Test
    public void costsCalculatedCorrectly() {
        Vertex v1 = new Vertex(1, 10.0);
        Vertex v2 = new Vertex(2, 10.0);
        Edge e12 = new Edge(v2, 15.0);
        v1.addEdge(e12);

        SearchNode sn1 = new SearchNode(v1);
        SearchNode sn2 = new SearchNode(v2, sn1, e12);

        assertTrue(MathUtil.isDoubleEqual(sn2.costsFromStart() + sn2.estimatedCostsToTarget(),
                sn2.estimatedTotalCosts()));
    }

    @Test
    public void compareSearchNodes() {
        //given
        Vertex v1 = new Vertex(1, 20.0);
        Vertex v2 = new Vertex(2, 10.0);
        Vertex v3 = new Vertex(3, 10.0);
        Edge e12 = new Edge(v2, 10.0);
        Edge e13 = new Edge(v3, 15.0);
        v1.addEdge(e12);
        v1.addEdge(e13);
        //when
        SearchNode sn1 = new SearchNode(v1);
        SearchNode sn2 = new SearchNode(v2, sn1, e12);
        SearchNode sn3 = new SearchNode(v3, sn1, e13);
        //then
        assertTrue(sn2.compareTo(sn3) < 0);
    }

    @DisplayName("SearchNode.equals when state equals nodes are equal")
    @Test
    void equalsWhenStateEqualsNodesAreEqual() {
        //given
        Vertex v = new Vertex(1, 20);
        SearchNode sn1 = new SearchNode(v);
        SearchNode sn2 = new SearchNode(v);
        //when
        boolean result = sn1.equals(sn2);
        //the
        assertTrue(result);
    }

    @DisplayName("SearchNode.equals when one node null does not throw exception")
    @Test
    void equalsWhenOneNodeNullDoesNotThrowException() {
        //given
        Vertex v = new Vertex(1, 20);
        SearchNode sn1 = new SearchNode(v);

        //when
        //then
        assertDoesNotThrow(() -> sn1.equals(null));
    }

    @DisplayName("SearchNode.equals when one node null returns false")
    @Test
    void equalsWhenOneNodeNullReturnsFalse() {
        //given
        Vertex v = new Vertex(1, 20);
        SearchNode sn1 = new SearchNode(v);

        //when
        //then
        assertNotEquals(null, sn1);
    }

    @DisplayName("SearchNode.equals when state null does not throw exception")
    @Test
    void equalsWhenStateNullDoesNotThrowException() {
        //given
        Vertex v = new Vertex(1, 20);
        SearchNode sn1 = new SearchNode(v);
        SearchNode sn2 = new SearchNode(null);

        //when
        //then
        assertDoesNotThrow(() -> sn1.equals(sn2));
    }

    @DisplayName("SearchNode.equals when one state null returns false")
    @Test
    void equalsWhenOneStateNullReturnsFalse() {
        //given
        Vertex v = new Vertex(1, 20);
        SearchNode sn1 = new SearchNode(v);
        SearchNode sn2 = new SearchNode(null);

        //when
        //then
        assertNotEquals(sn2, sn1);
    }

    @DisplayName("SearchNode.equals and hashCode are symmetric")
    @Test
    void equalsAndHashCodeAreSymmetric() {
        //given
        Vertex v = new Vertex(1, 20);
        SearchNode sn1 = new SearchNode(v);
        SearchNode sn2 = new SearchNode(v);

        //when
        boolean result = sn1.hashCode() == sn2.hashCode();

        //then
        assertTrue(result && sn1.equals(sn2));
    }

    @DisplayName("SearchNode.setPredecessor when set returns same")
    @Test
    void setPredecessorWhenSetReturnsSame() {
        //given
        SearchNode sn = new SearchNode(null);
        SearchNode expected = new SearchNode(null);

        //when
        sn.setPredecessor(expected);

        //then
        assertSame(expected, sn.getPredecessor());
    }

    @DisplayName("SearchNode.setPredecessor when set to null returns null")
    @Test
    void setPredecessorWhenSetToNullReturnsNull() {
        //given
        SearchNode sn = new SearchNode(null);

        //when
        sn.setPredecessor(null);

        //then
        assertNull(sn.getPredecessor());
    }

    @DisplayName("SearchNode.setTransiton when set returns same")
    @Test
    void setTransitionWhenSetReturnsSame() {
        //given
        SearchNode sn = new SearchNode(null);
        Edge expected = new Edge(new Vertex(1, 0), 0);

        //when
        sn.setTransition(expected);

        //then
        assertSame(expected, sn.getTransition());
    }

    @DisplayName("SearchNode.setTransiton when set to null returns null")
    @Test
    void setTransitionWhenSetToNullReturnsNull() {
        //given
        SearchNode sn = new SearchNode(null, null, new Edge(new Vertex(1, 0), 0));

        //when
        sn.setTransition(null);

        //then
        assertNull(sn.getTransition());
    }

    @DisplayName("SearchNode.costsFromStart when no predecessor is zero")
    @Test
    void costsFromStartWhenNoPredecessorIsZero() {
        //given
        SearchNode sn = new SearchNode(null);

        //when
        //then
        assertEquals(0, sn.costsFromStart());
    }

    @DisplayName("SearchNode.setCostsFromStart does set costsFromStart")
    @Test
    void setCostsFromStartDoesSetCostsFromStart() {
        //given
        SearchNode sn = new SearchNode(null);

        //when
        sn.setCostsFromStart(10);

        //then
        assertEquals(10, sn.costsFromStart());
    }

    @DisplayName("SearchNode.getTransitionsFromStart does not include null transitions")
    @Test
    void getTransitionsFromStartDoesNotIncludeNullTransitions() {
        //given
        SearchNode sn = new SearchNode(null);
        sn.setTransition(null);

        //when
        List<? extends Transition> result = sn.getTransitionsFromStart();
        //then
        assertTrue(result.isEmpty());
    }

    @DisplayName("SearchNode.getTransitionsFromStart includes transitions in correct order")
    @Test
    void getTransitionsFromStartIncludesTransitionsInCorrectOrder() {
        //given
        Vertex v1 = new Vertex(1, 10);
        Vertex v2 = new Vertex(2, 10);
        Vertex v3 = new Vertex(2, 10);
        Edge e1 = new Edge(v1, 10);
        Edge e2 = new Edge(v2, 10);
        SearchNode sn1 = new SearchNode(v1);
        SearchNode sn2 = new SearchNode(v2, sn1, e1);
        SearchNode sn3 = new SearchNode(v3, sn2, e2);

        //when
        List<Edge> result = (List<Edge>) sn3.getTransitionsFromStart();
        //then
        assertSame(e2, result.get(1));
        assertSame(e1, result.get(0));
    }

}
