package swe4.astar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AStarSolverTest {

    private static class Vertex implements State {

        private final int id;
        private final double costs;
        private final List<Transition> transitions = new ArrayList<>();

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

        private final Vertex to;
        private final double costs;

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
    void singleStateSolutionExists() {
        AStarSolver solver = new AStarSolver();
        Vertex finalState = new Vertex(1, 0.0);
        try {
            List<? extends Transition> transitions = solver.solve(finalState);
            assertTrue(transitions.isEmpty());
        } catch (NoSolutionException e) {
            fail("NoSolutionException not expected");
        }
    }

    @Test
    void singleStateSolutionDoesNotExist() {
        AStarSolver solver = new AStarSolver();
        Vertex finalState = new Vertex(1, 1.0);
        assertThrows(NoSolutionException.class, () -> solver.solve(finalState));
    }

    @Test
    void oneTransition() {
        AStarSolver solver = new AStarSolver();
        Vertex initial = new Vertex(1, 1.0);
        Vertex terminal = new Vertex(2, 0.0);
        initial.addEdge(new Edge(terminal, 1.0));

        try {
            List<? extends Transition> transitions = solver.solve(initial);
            assertEquals(1, transitions.size());
            assertEquals(terminal, initial.apply(transitions.get(0)));
        } catch (NoSolutionException e) {
            fail("NoSolutionException not expected");
        }
    }

    @Test
    void twoNodesTwoTransition() {
        AStarSolver solver = new AStarSolver();

        Vertex initial = new Vertex(1, 1.0);
        Vertex terminal = new Vertex(2, 0.0);
        Edge e1 = new Edge(terminal, 3.0);
        Edge e2 = new Edge(terminal, 2.0);
        initial.addEdge(e1);
        initial.addEdge(e2);

        try {
            List<? extends Transition> transitions = solver.solve(initial);
            assertEquals(1, transitions.size());
            assertSame(e2, transitions.get(0));
            assertEquals(terminal, initial.apply(transitions.get(0)));
        } catch (NoSolutionException e) {
            fail("NoSolutionException not expected");
        }
    }

    @Test
    void triangleSumTwoSidesShorter() {
        AStarSolver solver = new AStarSolver();

        Vertex v1 = new Vertex(1, 8.0);
        Vertex v2 = new Vertex(2, 5);
        Vertex v3 = new Vertex(2, 0.0);

        Edge e13 = new Edge(v3, 10.0);
        v1.addEdge(e13);
        Edge e12 = new Edge(v2, 3.0);
        v1.addEdge(e12);
        Edge e23 = new Edge(v3, 4.0);
        v2.addEdge(e23);

        try {
            List<? extends Transition> transitions = solver.solve(v1);
            assertEquals(2, transitions.size());
            assertSame(e12, transitions.get(0));
            assertSame(e23, transitions.get(1));
        } catch (NoSolutionException e) {
            fail("NoSolutionException not expected");
        }
    }

    @Test
    void triangleOneSideShorter() {
        AStarSolver solver = new AStarSolver();

        Vertex v1 = new Vertex(1, 8.0);
        Vertex v2 = new Vertex(2, 5);
        Vertex v3 = new Vertex(3, 0.0);

        Edge e13 = new Edge(v3, 10.0);
        v1.addEdge(e13);
        Edge e12 = new Edge(v2, 6.0);
        v1.addEdge(e12);
        Edge e23 = new Edge(v3, 7.0);
        v2.addEdge(e23);

        try {
            List<? extends Transition> transitions = solver.solve(v1);
            assertEquals(1, transitions.size());
            assertSame(e13, transitions.get(0));
        } catch (NoSolutionException e) {
            fail("NoSolutionException not expected");
        }
    }

    @Test
    void quadrangle() {
        AStarSolver solver = new AStarSolver();

        Vertex v1 = new Vertex(1, 8.0);
        Vertex v2 = new Vertex(2, 4.0);
        Vertex v3 = new Vertex(3, 7.0);
        Vertex v4 = new Vertex(4, 0.0);

        Edge e12 = new Edge(v2, 6.0);
        v1.addEdge(e12);
        Edge e13 = new Edge(v3, 4.0);
        v1.addEdge(e13);
        Edge e32 = new Edge(v2, 1.0);
        v3.addEdge(e32);
        Edge e24 = new Edge(v4, 5.0);
        v2.addEdge(e24);
        Edge e34 = new Edge(v4, 8.0);
        v3.addEdge(e34);

        try {
            List<? extends Transition> transitions = solver.solve(v1);
            assertEquals(3, transitions.size());
            assertSame(e13, transitions.get(0));
            assertSame(e32, transitions.get(1));
            assertSame(e24, transitions.get(2));
        } catch (NoSolutionException e) {
            fail("NoSolutionException not expected");
        }
    }

    /**
     * Here is the graph: https://i.imgur.com/bsMXbB7.png
     */
    @DisplayName("AStar.solve when complex graph does not throw NoSolutionException")
    @Test
    void solveWhenComplexGraphDoesNotThrowNoSolutionException() {
        //given
        AStarSolver solver = new AStarSolver();
        int count = 0;
        Vertex a = new Vertex(count++, 40);
        Vertex b = new Vertex(count++, 35);
        Vertex c = new Vertex(count++, 38);
        Vertex d = new Vertex(count++, 48);
        Vertex e = new Vertex(count++, 55);
        Vertex f = new Vertex(count++, 50);
        Vertex g = new Vertex(count++, 21);
        Vertex h = new Vertex(count++, 20);
        Vertex i = new Vertex(count++, 26);
        Vertex s = new Vertex(count++, 45);
        Vertex z = new Vertex(count++, 0);

        Edge ab = new Edge(b, 7);
        a.addEdge(ab);
        Edge ag = new Edge(g, 22);
        a.addEdge(ag);

        Edge bg = new Edge(g, 19);
        b.addEdge(bg);
        Edge bi = new Edge(i, 12);
        b.addEdge(bi);

        Edge cb = new Edge(b, 8);
        c.addEdge(cb);
        Edge ci = new Edge(i, 12);
        c.addEdge(ci);

        Edge dc = new Edge(c, 10);
        d.addEdge(dc);

        Edge ed = new Edge(d, 9);
        e.addEdge(ed);
        Edge ef = new Edge(f, 9);
        e.addEdge(ef);

        Edge fa = new Edge(a, 10);
        f.addEdge(fa);

        Edge gh = new Edge(h, 8);
        g.addEdge(gh);
        Edge gz = new Edge(z, 24);
        g.addEdge(gz);

        Edge hz = new Edge(z, 22);
        h.addEdge(hz);

        Edge ih = new Edge(h, 9);
        i.addEdge(ih);
        Edge iz = new Edge(z, 32);
        i.addEdge(iz);

        Edge sa = new Edge(a, 11);
        s.addEdge(sa);
        Edge sb = new Edge(b, 10);
        s.addEdge(sb);
        Edge sc = new Edge(c, 7);
        s.addEdge(sc);
        Edge sd = new Edge(d, 7);
        s.addEdge(sd);
        Edge se = new Edge(e, 10);
        s.addEdge(se);
        Edge sf = new Edge(f, 8);
        s.addEdge(sf);

        //when
        //then
        assertDoesNotThrow(() -> solver.solve(s));
    }

    @DisplayName("AStar.solve when complex graph returns correct transitions")
    @Test
    void solveWhenComplexGraphReturnsCorrectTransitions() {
        //given
        AStarSolver solver = new AStarSolver();
        int count = 0;
        Vertex a = new Vertex(count++, 40);
        Vertex b = new Vertex(count++, 35);
        Vertex c = new Vertex(count++, 38);
        Vertex d = new Vertex(count++, 48);
        Vertex e = new Vertex(count++, 55);
        Vertex f = new Vertex(count++, 50);
        Vertex g = new Vertex(count++, 21);
        Vertex h = new Vertex(count++, 20);
        Vertex i = new Vertex(count++, 26);
        Vertex s = new Vertex(count++, 45);
        Vertex z = new Vertex(count++, 0);

        Edge ab = new Edge(b, 7);
        a.addEdge(ab);
        Edge ag = new Edge(g, 22);
        a.addEdge(ag);

        Edge bg = new Edge(g, 19);
        b.addEdge(bg);
        Edge bi = new Edge(i, 12);
        b.addEdge(bi);

        Edge cb = new Edge(b, 8);
        c.addEdge(cb);
        Edge ci = new Edge(i, 12);
        c.addEdge(ci);

        Edge dc = new Edge(c, 10);
        d.addEdge(dc);

        Edge ed = new Edge(d, 9);
        e.addEdge(ed);
        Edge ef = new Edge(f, 9);
        e.addEdge(ef);

        Edge fa = new Edge(a, 10);
        f.addEdge(fa);

        Edge gh = new Edge(h, 8);
        g.addEdge(gh);
        Edge gz = new Edge(z, 24);
        g.addEdge(gz);

        Edge hz = new Edge(z, 22);
        h.addEdge(hz);

        Edge ih = new Edge(h, 9);
        i.addEdge(ih);
        Edge iz = new Edge(z, 32);
        i.addEdge(iz);

        Edge sa = new Edge(a, 11);
        s.addEdge(sa);
        Edge sb = new Edge(b, 10);
        s.addEdge(sb);
        Edge sc = new Edge(c, 7);
        s.addEdge(sc);
        Edge sd = new Edge(d, 7);
        s.addEdge(sd);
        Edge se = new Edge(e, 10);
        s.addEdge(se);
        Edge sf = new Edge(f, 8);
        s.addEdge(sf);

        List<? extends Transition> expected = List.of(sc, ci, ih, hz);

        try {
            //when
            var result = solver.solve(s);
            //then
            for (int j = 0; j < result.size(); j++) {
                assertSame(expected.get(j), result.get(j));
            }
        } catch(NoSolutionException nse) {
            fail("Unexpected Exception");
        }
    }

    @DisplayName("AStarSolver.solve when graph looped throws NoSolutionException")
    @Test
    void solveWhenGraphLoopedThrowsNoSolutionException() {
        //given
        Vertex v1 = new Vertex(1, 10);
        Vertex v2 = new Vertex(2, 8);
        Vertex v3 = new Vertex(3, 9);
        Edge e12 = new Edge(v2, 5);
        v1.addEdge(e12);
        Edge e23 = new Edge(v3, 5);
        v2.addEdge(e23);
        Edge e31 = new Edge(v1, 5);
        v3.addEdge(e31);

        AStarSolver solver = new AStarSolver();

        //when
        //then
        assertThrows(NoSolutionException.class, () -> solver.solve(v1));
    }
}


