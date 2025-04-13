package cc.helium.util.cal;

import java.security.SecureRandom;

/**
 * @author Kev1nLeft
 */

public class Mth {
    public static final float PI = (float) Math.PI;
    public static final float TO_DEGREES = 180.0F / PI;

    public static double wrappedDifference(double number1, double number2) {
        return Math.min(Math.abs(number1 - number2), Math.min(Math.abs(number1 - 360) - Math.abs(number2 - 0), Math.abs(number2 - 360) - Math.abs(number1 - 0)));
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0D / inc;
        return (double)Math.round(val * one) / one;
    }

    public static double clamp(double num, double min, double max) {
        if (num < min) {
            return min;
        }
        return Math.min(num, max);
    }

    public static int getRandomInRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static float getRandomInRange(float min, float max) {
        SecureRandom random = new SecureRandom();
        return random.nextFloat() * (max - min) + min;
    }

    public static double getRandomInRange(double min, double max) {
        SecureRandom random = new SecureRandom();
        return min == max ? min : random.nextDouble() * (max - min) + min;
    }


}
