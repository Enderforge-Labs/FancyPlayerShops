package com.snek.fancyplayershops.configs;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworkconfig.ConfigManager;
import com.snek.fancyplayershops.main.FancyPlayerShops;








/**
 * A utility class that contains configuration file data.
 */
public abstract class Configs {

    private Configs() {}

    private static @NotNull DisplayConfig     display = null;
    private static @NotNull PerformanceConfig perf    = null;
    private static @NotNull MiscConfig        misc    = null;

    public static @NotNull DisplayConfig      getDisplay() { return display; }
    public static @NotNull PerformanceConfig  getPerf   () { return perf;    }
    public static @NotNull MiscConfig         getMisc   () { return misc;    }




    /**
     * Loads the configuration files or creates new ones if they are missing.
     */
    public static void loadConfigs() {
        display = ConfigManager.loadConfig("DisplayConfig",     DisplayConfig.class,     FancyPlayerShops.MOD_ID);
        perf    = ConfigManager.loadConfig("PerformanceConfig", PerformanceConfig.class, FancyPlayerShops.MOD_ID);
        misc    = ConfigManager.loadConfig("MiscConfig",        MiscConfig.class,        FancyPlayerShops.MOD_ID);
    }
}
