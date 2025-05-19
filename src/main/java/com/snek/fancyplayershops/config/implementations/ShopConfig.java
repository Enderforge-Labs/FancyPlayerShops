package com.snek.fancyplayershops.config.implementations;

import java.math.BigInteger;

import com.snek.fancyplayershops.config.ConfigFile;
import com.snek.fancyplayershops.config.fields.ConstrainedConfigField;
import com.snek.fancyplayershops.config.fields.DefaultConfigField;
import com.snek.fancyplayershops.config.fields.ValueConfigField;








public class ShopConfig implements ConfigFile {
    public final ConstrainedConfigField<Long> price = new ConstrainedConfigField<>(
        new String[] {
            "The price players are allowed to set.",
            "Must be >= 0.",
            "(price.max * stock.max) must be between 0 and Long.MAX_VALUE."
        },
        0l,
        1_000l * 100l,
        10_000_000_000l * 100l
    );
    public final ConstrainedConfigField<Integer> stock_limit = new ConstrainedConfigField<>(
        new String[] {
            "The stock limit players are allowed to set.",
            "Must be >= 0.",
            "(price.max * stock.max) must be between 0 and Long.MAX_VALUE."
        },
        0,
        1_000,
        1_000_000
    );
    public final ValueConfigField<Float[]> theme_hues = new ValueConfigField<>(
        new String[] {
            "The hues of the color themes. Each element represents a new theme.",
            "At least 1 hue value is required.",
            "Values must be between 0.0 and 360.0"
        },
        new Float[] { 0f, 25f, 55f, 95f, 135f, 180f, 220f, 260f, 300f }
    );
    public final DefaultConfigField<Integer> theme = new DefaultConfigField<>(
        new String[] {
            "The index of the default color theme.",
            "Starts from 0. Must be < theme_hues.length"
        },
        7
    );
    public final ValueConfigField<Float> theme_saturation_main = new ValueConfigField<>(
        new String[] {
            "The saturation value of the main color of themes.",
            "Must be between 0.0 and 1.0"
        },
        0.2f
    );
    public final ValueConfigField<Float> theme_luminosity_main = new ValueConfigField<>(
        new String[] {
            "The luminosity value of the main color of themes.",
            "Must be between 0.0 and 1.0"
        },
        0.75f
    );
    public final ValueConfigField<Float> theme_saturation_secondary = new ValueConfigField<>(
        new String[] {
            "The saturation value of the secondary color of themes.",
            "Must be between 0.0 and 1.0"
        },
        0.4f
    );
    public final ValueConfigField<Float> theme_luminosity_secondary = new ValueConfigField<>(
        new String[] {
            "The luminosity value of the secondary color of themes.",
            "Must be between 0.0 and 1.0"
        },
        0.3f
    );








    @Override
    public void validate(){

        // Check price
        if(price.getMin    () < 0)              throw new IllegalStateException("Minimum price must be >= 0");
        if(price.getDefault() < 0)              throw new IllegalStateException("Default price must be >= 0");
        if(price.getMax    () < 0)              throw new IllegalStateException("Maximum price must be >= 0");
        if(price.getDefault() < price.getMin()) throw new IllegalStateException("Default price must be >= price.min");
        if(price.getDefault() > price.getMax()) throw new IllegalStateException("Default price must be <= price.max");


        // Check stock limit
        if(stock_limit.getMin    () < 0)                    throw new IllegalStateException("Minimum stock limit must be >= 0");
        if(stock_limit.getDefault() < 0)                    throw new IllegalStateException("Default stock limit must be >= 0");
        if(stock_limit.getMax    () < 0)                    throw new IllegalStateException("Maximum stock limit must be >= 0");
        if(stock_limit.getDefault() < stock_limit.getMin()) throw new IllegalStateException("Default stock limit must be >= stock_limit.min");
        if(stock_limit.getDefault() > stock_limit.getMax()) throw new IllegalStateException("Default stock limit must be <= stock_limit.max");


        // Check color theme index
        final Float[] h = theme_hues.getValue();
        if(h.length < 1) throw new IllegalStateException("The list of theme hues must contain at least 1 element");
        if(theme.getDefault() < 0) throw new IllegalStateException("The index of the default theme must be >= 0");
        if(theme.getDefault() >= h.length) throw new IllegalStateException("The index of the default theme must be < themeHues.length");
        for(int i = 0; i < h.length; ++i) {
            if(h[i] < 0) throw new IllegalStateException("Hue value of color theme #" + i + " must be >= 0");
            if(h[i] > 1) throw new IllegalStateException("Hue value of color theme #" + i + " must be <= 1");
        }


        // Check color theme saturation and value
        if(theme_saturation_main     .getValue() < 0f) throw new IllegalStateException("Saturation of main theme color must be >= 0");
        if(theme_saturation_main     .getValue() > 1f) throw new IllegalStateException("Saturation of main theme color must be <= 1");
        if(theme_luminosity_main     .getValue() < 0f) throw new IllegalStateException("Luminosity of main theme color must be >= 0");
        if(theme_luminosity_main     .getValue() > 1f) throw new IllegalStateException("Luminosity of main theme color must be <= 1");
        if(theme_saturation_secondary.getValue() < 0f) throw new IllegalStateException("Saturation of secondary theme color must be >= 0");
        if(theme_saturation_secondary.getValue() > 1f) throw new IllegalStateException("Saturation of secondary theme color must be <= 1");
        if(theme_luminosity_secondary.getValue() < 0f) throw new IllegalStateException("Luminosity of secondary theme color must be >= 0");
        if(theme_luminosity_secondary.getValue() > 1f) throw new IllegalStateException("Luminosity of secondary theme color must be <= 1");


        // Check maximum possible price
        final BigInteger _price = BigInteger.valueOf(price.getMax());
        final BigInteger _stock = BigInteger.valueOf(stock_limit.getMax());
        final BigInteger product = _price.multiply(_stock);
        final int excess = product.toString().length() - Long.toString(Long.MAX_VALUE).length();
        if(product.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
            throw new IllegalStateException(
                "Maximum possible transaction price is above the Long limit by " + excess + (excess == 1 ? " digit." : " digits.") +
                " Adjust the price.max and stock.max config values.")
            ;
        }
    }
}
