package com.snek.fancyplayershops.configs;

import com.snek.frameworkconfig.config.ConfigFile;
import com.snek.frameworkconfig.config.fields.ValueConfigField;








public class MiscConfig implements ConfigFile {
    public final ValueConfigField<Long> confirm_timeout = new ValueConfigField<>(
        new String[] {
            "The amount of time before command confirmations expire, in ticks.",
            "Must be > 20"
        },
        20L * 5L
    );








    @Override
    public void validate() {

        // Check confirmation timeout
        if(confirm_timeout.getValue() < 20L) throw new IllegalStateException("Confirmation timeout must be >= 20");
    }
}
