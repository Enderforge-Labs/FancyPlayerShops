package com.snek.fancyplayershops.main;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.config.ConfigManager;
import com.snek.fancyplayershops.config.implementations.PerformanceConfig;
import com.snek.fancyplayershops.config.implementations.ShopConfig;
import com.snek.fancyplayershops.config.implementations.UiConfig;








/**
 * A utility class that contains configuration file data.
 */
public abstract class Configs {
    private Configs() {}
    public static @NotNull ShopConfig shop = null;
    public static @NotNull PerformanceConfig perf = null;
    public static @NotNull UiConfig ui = null;




    /**
     * Loads the configuration files or creates new ones if they are missing.
     */
    public static void loadConfigs() {
        shop = ConfigManager.loadConfig("ShopConfig",  ShopConfig.class);
        perf = ConfigManager.loadConfig("Performance", PerformanceConfig.class);
        ui   = ConfigManager.loadConfig("UiConfig",    UiConfig.class);
    }
}
