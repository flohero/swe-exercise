package swe4.astar;

import java.util.List;

// Repräsentiert den Zustand eines Knotens im Suchraum. Dieser ist über
// Transitionen mit seinen Nachbarzuständen verbunden. Auf diese Weise
// wird ein Zustandsgraph aufgespannt.
public interface State extends Comparable<State> {

  // Überprüft, ob dieser Zustand gleich dem Zustand other ist.
  @Override
  boolean equals(Object obj);

  // Notwendig für Verwendung in Hash-basierten Behältern.
  // Muss mit equals kompatibel sein:
  // state1.equals(state2) => state1.hashCode() == state2.hashCode()
  @Override
  int hashCode();

  // Liste der Zustandsüberführungen, mit denen man von diesem Zustand in
  // die unmittelbar anschließenden Zustände gelangt.
  List<? extends Transition> transitions();

  // Retourniert den Knoten, der durch Anwendung der Zustandsüberführung
  // transition auf diesen Zustand entsteht.
  <S extends State> S apply(Transition transition);

  // Geschätzte Kosten zum Erreichen des Zielzustands. Werden diese immer
  // unterschätzt, ermittelt der A*-Algorithmus die minimale Lösung.
  double estimatedCostsToTarget();
}
