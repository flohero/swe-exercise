package swe4.astar;

// Repräsentation einer Zustandsüberführung. Jeder Zustand hält eine Liste
// von Transitionen, über die man zu den Nachbarzuständen gelangen kann.
public interface Transition {
  // Tatsächliche Kosten für diese Zustandsüberführung
  double costs();
}
