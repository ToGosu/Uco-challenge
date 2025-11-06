package co.edu.uco.ucochallenge.crosscuting.helper;

public final class NumberHelper {

    public static final int ZERO = 0;
    public static final long ZERO_LONG = 0L;
    public static final double ZERO_DOUBLE = 0.0;

    private NumberHelper() {
    }

    public static int getDefaultInt() {
        return ZERO;
    }

    public static int getDefault(final Integer value) {
        return ObjectHelper.getDefault(value, getDefaultInt());
    }

    public static long getDefaultLong() {
        return ZERO_LONG;
    }

    public static long getDefault(final Long value) {
        return ObjectHelper.getDefault(value, getDefaultLong());
    }

    public static double getDefaultDouble() {
        return ZERO_DOUBLE;
    }

    public static double getDefault(final Double value) {
        return ObjectHelper.getDefault(value, getDefaultDouble());
    }

    public static boolean isPositive(final int value) {
        return value > ZERO;
    }

    public static boolean isNegative(final int value) {
        return value < ZERO;
    }

    public static boolean isZero(final int value) {
        return value == ZERO;
    }
}