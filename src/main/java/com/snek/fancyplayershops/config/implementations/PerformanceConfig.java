package com.snek.fancyplayershops.config.implementations;

import com.snek.fancyplayershops.config.ConfigFile;
import com.snek.fancyplayershops.config.fields.ValueConfigField;








public class PerformanceConfig implements ConfigFile {
    public final ValueConfigField<Float> reach_distance = new ValueConfigField<>(
        new String[] {
            "The maximum distance at which players can interact with shops. Measured in Blocks.",
            "Must be between 0.5 and 8.0.",
            "Higher values can impact performance."
        },
        5f
    );
    public final ValueConfigField<Float> ray_casting_step = new ValueConfigField<>(
        new String[] {
            "The distance between ray casting steps. Measured in Blocks.",
            "Must be between 0.02 and 0.5.",
            "Higher values provide higher accuracy and responsiveness but can easily degrade performance."
        },
        0.2f
    );
    public final ValueConfigField<Integer> animation_refresh_time = new ValueConfigField<>(
        new String[] {
            "The time between transition updates. Measured in ticks.",
            "Must be between 1 and 10.",
            "Lower values create sharper and more accurate animations but increase server load and client lag."
        },
        2
    );
    public final ValueConfigField<Integer> pulls_per_tick = new ValueConfigField<>(
        new String[] {
            "The maximum number of shops to push items into per tick.",
            "Must be >= 1.",
            "Higher values decrease the total duration of pull cycles, but can severly impact performance.",
        },
        2
    );
    public final ValueConfigField<Integer> pull_cycle_cooldown = new ValueConfigField<>(
        new String[] {
            "The minimum time between item pull cycles. Measured in ticks.",
            "Must be >= 1.",
            "Lower values increase pull frequency but can reduce performance when a lot of unloaded shops are present.",
            "Values above 20 are generally safe."
        },
        5 * 20
    );








    @Override
    public void validate(){

        // Check reach distance
        if(reach_distance.getValue() < 0.5) throw new IllegalStateException("Reach distance must be >= 0.5");
        if(reach_distance.getValue() > 8)   throw new IllegalStateException("Reach distance must be <= 8");


        // Check step size
        if(ray_casting_step.getValue() < 0.02) throw new IllegalStateException("Ray casting step size must be >= 0.02");
        if(ray_casting_step.getValue() > 0.5)  throw new IllegalStateException("Ray casting step size must be <= 0.5");


        // Check animation refresh time
        if(animation_refresh_time.getValue() < 1)  throw new IllegalStateException("Animation refresh time must be >= 1");
        if(animation_refresh_time.getValue() > 10) throw new IllegalStateException("Animation refresh time must be <= 10");
    }
}
