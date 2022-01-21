package org.springframework.assessmentjob.util;

public final class RateLimits {

    private RateLimits() {
        throw new IllegalStateException("Can't instantiate a utility class.");
    }

    public static void shortDelay() {
        delay(10000);
    }

    public static void longDelay() {
        delay(30000);
    }

    private static void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
