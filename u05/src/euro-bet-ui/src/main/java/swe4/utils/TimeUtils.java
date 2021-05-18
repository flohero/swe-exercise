package swe4.utils;

import java.time.LocalDateTime;

public class TimeUtils {

    private TimeUtils() {
        throw new AssertionError("No TimeUtils instances for you!");
    }

    public static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return (start1.isBefore(end2) && start1.isAfter(start2))
                || (start2.isBefore(end1) && start2.isAfter(start1))
                || start1.isEqual(start2);
    }
}
