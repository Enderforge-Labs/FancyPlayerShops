package com.snek.fancyplayershops.configs;

import com.snek.frameworkconfig.ConfigFile;
import com.snek.frameworkconfig.fields.ValueConfigField;








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
            "The distance between ray casting steps for shop focus detection. Measured in Blocks.",
            "Must be between 0.02 and 0.5.",
            "Higher values provide higher accuracy and responsiveness but can easily degrade performance."
        },
        0.2f
    );

    // This config is intentionally kept here for FancyPlayerShops-specific tuning
    public final ValueConfigField<Integer> ray_casting_batches = new ValueConfigField<>(
        new String[] {
            "The amount of batches needed to complete a ray casting check cycle.",
            "Must be >= 1",
            "Player ray casting checks are performed every tick, but they can be split into batches to improve performance.",
            "e.g. 1 batch means that every player is checked every tick. 2 batches means that half the players are checked every even tick, while the other half is checked every odd tick.",
            "Higher values improve performance but increase shop hover detection delay by up to (ray_casting_batches + 1) ticks."
        },
        4
    );
    public final ValueConfigField<Integer> pulls_per_tick = new ValueConfigField<>(
        new String[] {
            "The maximum number of shops to push items into per tick.",
            "Must be >= 1.",
            "Higher values decrease the total duration of pull cycles, but can severly impact performance.",
        },
        2
    );
    public final ValueConfigField<Integer> pull_cycle_frequency = new ValueConfigField<>(
        new String[] {
            "The minimum time between item pull cycles. Measured in ticks.",
            "Must be >= 1.",
            "Lower values increase pull frequency but can reduce performance when a lot of unloaded shops are present.",
            "Values above 20 are generally safe."
        },
        5 * 20
    );
    public final ValueConfigField<Integer> data_save_frequency = new ValueConfigField<>(
        new String[] {
            "The time between shop and stash data saves. Measured in ticks.",
            "Must be >= 1.",
            "Only modified shops and stashes are saved to file, so this config has little inpact on performance.",
            "Very high values can occasionally create lag spikes. Lower values improve safety and tps stability but slightly degrade performance.",
        },
        4
    );








    @Override
    public void validate() {

        // Check reach distance
        if(reach_distance.getValue() < 0.5) throw new IllegalStateException("Reach distance must be >= 0.5");
        if(reach_distance.getValue() > 8)   throw new IllegalStateException("Reach distance must be <= 8");


        // Check ray casting step size
        if(ray_casting_step.getValue() < 0.02) throw new IllegalStateException("Ray casting step size must be >= 0.02");
        if(ray_casting_step.getValue() > 0.5)  throw new IllegalStateException("Ray casting step size must be <= 0.5");


        // Check ray casting batch size
        if(ray_casting_batches.getValue() < 1) throw new IllegalStateException("Ray casting batches must be >= 1");


        // Check data save frequency
        if(data_save_frequency.getValue() < 1) throw new IllegalStateException("Data save frequency must be >= 1");
        //TODO use custom invalid config file option exception
    }
}
