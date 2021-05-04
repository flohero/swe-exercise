package swe4.astar;

public class MathUtil {

  private static final double EPSILON = 1E-6;

  public static boolean isDoubleEqual(double a, double b) {
    return Math.abs(a-b) < EPSILON;
  }

  public static boolean isDoubleZero(double a) {
    return Math.abs(a) < EPSILON;
  }

}
