package com.snek.fancyplayershops.config.implementations;

import java.math.BigInteger;

import com.snek.fancyplayershops.config.ConfigFile;
import com.snek.fancyplayershops.config.fields.ConstrainedConfigField;
import com.snek.fancyplayershops.config.fields.FreeConfigField;








public class ShopConfig implements ConfigFile {
    public final ConstrainedConfigField<Long> price = new ConstrainedConfigField<>(
        "The price players are allowed to set. [0 <= price.max * stock.max <= Long.MAX_VALUE]",
        0l,
        1_000l * 100l,
        10_000_000_000l * 100l
    );
    public final ConstrainedConfigField<Integer> stock = new ConstrainedConfigField<>(
        "The stock limit players are allowed to set. [0 <= price.max * stock.max <= Long.MAX_VALUE]",
        0,
        1_000,
        1_000_000
    );
    public final FreeConfigField<Float> theme_hue = new FreeConfigField<>(
        "The default hue of the color theme. [0.0 <= value <= 360.0]",
        300f
    );


    @Override
    public void validate(){
        final BigInteger _price = BigInteger.valueOf(price.getMax());
        final BigInteger _stock = BigInteger.valueOf(stock.getMax());
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
