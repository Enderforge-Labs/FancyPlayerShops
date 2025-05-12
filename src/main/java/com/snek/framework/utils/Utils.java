package com.snek.framework.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4i;








/**
 * A utility class that provides a collection of useful methods.
 */
public abstract class Utils {
    private Utils() {}

    // Formatters
    private static final DecimalFormat formatterPrice  = new DecimalFormat("#,##0.##");
    private static final DecimalFormat formatterAmount = new DecimalFormat("#,###");




    /**
     * Checks if a double value is within a certain threshold from a target value.
     * <p> This is used to avoid precision related problems when comparing double values.
     * @param n The value to check
     * @param target The target value
     * @param threshold The threshold to use
     * @return True if the value is withing the threshold, false otherwise
     */
    public static boolean doubleEquals(final double n, final double target, final double threshold) {
        return !(n < target - threshold || n > target + threshold);
    }


    /**
     * Invokes a Method on the object target using the specified arguments.
     * @param method The method to invoke.
     * @param target The target Object.
     * @param args The arguments to use. Can be empty.
     * @return The return value of the method.
     */
    public static @Nullable Object invokeSafe(final @NotNull Method method, final @NotNull Object target, final @Nullable Object... args) {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * Runs a task on a secondary thread after a specified delay.
     * @param delay The delay expressed in milliseconds.
     * @param task The task to run.
     */
    public static void runAsync(final int delay, final @NotNull Runnable task) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                Thread.currentThread().interrupt();
            }
            task.run();
        }).start();
    }










    /**
     * Returns the value <price> expressed as a string and formatted using $ as currency symbol, abbreviating the number to 3 digits.
     * @param price The price to format.
     * @return The formatted price.
     */
    public static @NotNull String formatPriceShort(final double price) {
        return formatPriceShort(price, "$");
    }


    /**
     * Returns the value <price> expressed as a string and formatted as specified, abbreviating the number to 3 digits.
     * @param price The price to format.
     * @param currency The currency symbol to use as prefix.
     * @return The formatted price.
     */
    public static @NotNull String formatPriceShort(final double price, final @NotNull String currency) {
        final String[] suffixes = { "", "k", "m", "b", "t", "q" };
        int exp = 0;
        double scaled = price;

        // Calculate exponent
        while(scaled >= 1000 && exp < suffixes.length - 1) {
            scaled /= 1000;
            exp++;
        }

        // Find optimal formatting
        String formatted;
        if     (scaled < 10 ) formatted = String.format("%.2f", scaled);
        else if(scaled < 100) formatted = String.format("%.1f", scaled);
        else                  formatted = String.format("%.0f", scaled);

        // Trim trailing .00 or .0
        if (formatted.contains(".")) {
            formatted = formatted.replaceAll("\\.?0+$", "");
        }

        // Return the formatted price with prefix and suffix added
        return currency + formatted + suffixes[exp];
    }




    /**
     * Returns the value <price> expressed as a string and formatted using $ as currency symbol and thousands separators.
     * @param price The price to format.
     * @return The formatted price.
     */
    public static @NotNull String formatPrice(final double price) {
        return formatPrice(price, "$", true);
    }


    /**
     * Returns the value <price> expressed as a string and formatted as specified.
     * @param price The price to format.
     * @param currency The currency symbol to use as prefix.
     * @param thousandsSeparator Whether to use a separator between thousands. [default: true]
     * @return The formatted price.
     */
    public static @NotNull String formatPrice(final double price, final @NotNull String currency, final boolean thousandsSeparator) {
        final String r;

        // Separator
        if(thousandsSeparator) {
            r = currency + formatterPrice.format(price);
        }

        // No separator
        else {
            r = String.format("%s%.2f", currency, price);
        }

        // Add trailing 0 if there is only one decimal digit
        return r.charAt(r.length() - 2) == '.' ? r + "0" : r;
    }








    /**
     * Returns the value <amount> expressed as a string and formatted using thousands separators.
     * @param amount The amount to format.
     * @return The formatted price.
     */
    public static @NotNull String formatAmount(final double amount) {
        return formatAmount(amount, false, true);
    }

    /**
     * Returns the value <amount> expressed as a string and formatted as specified.
     * @param amount The amount to format.
     * @param x Whether the amount should be prefixed with a lowercase "x".
     * @param thousandsSeparator Whether to use a separator between thousands.
     * @return The formatted price.
     */
    public static @NotNull String formatAmount(final double amount, final boolean x, final boolean thousandsSeparator) {
        final String r;

        // Separator
        if(thousandsSeparator) {
            r = formatterAmount.format(amount);
        }

        // No separator
        else {
            r = String.valueOf(amount);
        }

        // Add trailing x if requested
        return x ? r + "x" : r;
    }




    public static @NotNull Vector3i toBW(final @NotNull Vector3i rgb) {
        return HSVtoRGB(toBW(RGBtoHSV(rgb)));
    }
    public static @NotNull Vector3f toBW(final @NotNull Vector3f hsv) {
        return hsv.mul(1, 0, 1, new Vector3f());
    }




    /**
     * Converts an RGB color to HSV.
     * <p> Hue:         0 to 360.0
     * <p> Saturation:  0 to 1.0
     * <p> Value:       0 to 1.0
     * @param rgb The RGB color.
     * @return The color as an HSV value.
     */
    public static @NotNull Vector3f RGBtoHSV(final @NotNull Vector3i rgb) {
        final float r = rgb.x / 255.0f;
        final float g = rgb.y / 255.0f;
        final float b = rgb.z / 255.0f;

        final float max = Math.max(r, Math.max(g, b));
        final float min = Math.min(r, Math.min(g, b));
        final float delta = max - min;

        float h = 0;
        final float s;
        final float v = max;


        if(max != 0) {
            s = delta / max;
        }
        else {
            s = 0;
            h = -1;
            return new Vector3f(h, s, v);
        }

        if(delta == 0) {
            h = 0;
        }
        else if(r == max) {
            h = (g - b) / delta;
        }
        else if(g == max) {
            h = 2 + (b - r) / delta;
        }
        else {
            h = 4 + (r - g) / delta;
        }

        h *= 60;
        if(h < 0) h += 360;

        return new Vector3f(h, s, v);
    }




    /**
     * Converts an HSV color to RGB.
     * <p> Red:   0 to 255
     * <p> Green: 0 to 255
     * <p> Blue:  0 to 255
     * @param hsv The HSV color.
     * @return The color as an HSV value.
     */
    public static @NotNull Vector3i HSVtoRGB(final @NotNull Vector3f hsv) {
        final float h = hsv.x;
        final float s = hsv.y;
        final float v = hsv.z;

        final float c = v * s;
        final float x = c * (1 - Math.abs(((h / 60) % 2) - 1));
        final float m = v - c;

        float r = 0;
        float g = 0;
        float b = 0;

        if(0 <= h && h < 60) {
            r = c; g = x; b = 0;
        }
        else if(60 <= h && h < 120) {
            r = x; g = c; b = 0;
        }
        else if(120 <= h && h < 180) {
            r = 0; g = c; b = x;
        }
        else if(180 <= h && h < 240) {
            r = 0; g = x; b = c;
        }
        else if(240 <= h && h < 300) {
            r = x; g = 0; b = c;
        }
        else if(300 <= h && h < 360) {
            r = c; g = 0; b = x;
        }

        r += m; g += m; b += m;

        return new Vector3i(Math.round(r * 255), Math.round(g * 255), Math.round(b * 255));
    }




    /**
     * Interpolates two RGB colors while maintaining luminosity.
     * @param rgb1 The starting color.
     * @param rgb2 The target color
     * @param factor The interpolation factor.
     * @return The resulting color.
     */
    public static @NotNull Vector3i interpolateRGB(final @NotNull Vector3i rgb1, final @NotNull Vector3i rgb2, final float factor) {
        final Vector3f hsv1 = RGBtoHSV(rgb1);
        final Vector3f hsv2 = RGBtoHSV(rgb2);

        float h1 = hsv1.x;
        final float s1 = hsv1.y;
        final float v1 = hsv1.z;

        float h2 = hsv2.x;
        final float s2 = hsv2.y;
        final float v2 = hsv2.z;

        // Adjust hue to allow the interpolation to take the shortest path
        if(Math.abs(h1 - h2) > 180) {
            if(h1 > h2) h2 += 360;
            else h1 += 360;
        }

        // Interpolate values and return color vector
        return HSVtoRGB(new Vector3f(
            interpolateF(h1, h2, factor) % 360,
            interpolateF(s1, s2, factor),
            interpolateF(v1, v2, factor)
        ));
    }




    /**
     * Interpolates two ARGB colors while maintaining luminosity.
     * @param argb1 The starting color.
     * @param argb2 The target color
     * @param factor The interpolation factor.
     * @return The resulting color.
     */
    public static @NotNull Vector4i interpolateARGB(final @NotNull Vector4i argb1, final @NotNull Vector4i argb2, final float factor) {
        final Vector3i rgbRet = interpolateRGB(
            new Vector3i(argb1.y, argb1.z, argb1.w),
            new Vector3i(argb2.y, argb2.z, argb2.w), factor
        );
        return new Vector4i(
            interpolateI(argb1.x, argb2.x, factor),
            rgbRet.x,
            rgbRet.y,
            rgbRet.z
        );
    }




    /**
     * Interpolates two float values.
     * @param v1 The first value.
     * @param v2 The second value.
     * @param factor The interpolation factor.
     * @return The resulting value.
     */
    public static float interpolateF(final float v1, final float v2, final float factor) {
        return v1 + (v2 - v1) * factor;
    }




    /**
     * Interpolates two double values.
     * @param v1 The first value.
     * @param v2 The second value.
     * @param factor The interpolation factor.
     * @return The resulting value.
     */
    public static double interpolateF(final double v1, final double v2, final double factor) {
        return v1 + (v2 - v1) * factor;
    }




    /**
     * Interpolates two int values.
     * @param v1 The first value.
     * @param v2 The second value.
     * @param factor The interpolation factor.
     * @return The resulting value.
     */
    public static int interpolateI(final int v1, final int v2, final float factor) {
        return Math.round(v1 + (v2 - v1) * factor);
    }
}
