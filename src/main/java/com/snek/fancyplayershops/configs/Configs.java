package com.snek.fancyplayershops.configs;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworkconfig.ConfigManager;
import com.snek.fancyplayershops.main.FancyPlayerShops;








/**
 * A utility class that contains configuration file data.
 */
public abstract class Configs {

    private Configs() {}

    private static @NotNull ShopConfig        shop = null;
    private static @NotNull PerformanceConfig perf = null;

    public static @NotNull ShopConfig        getShop() { return shop; }
    public static @NotNull PerformanceConfig getPerf() { return perf; }




    /**
     * Loads the configuration files or creates new ones if they are missing.
     */
    public static boolean loadConfigs() {
        shop = ConfigManager.loadConfig("ShopConfig",         ShopConfig.class,        FancyPlayerShops.MOD_ID);
        perf = ConfigManager.loadConfig("PerformanceConfig",  PerformanceConfig.class, FancyPlayerShops.MOD_ID);
        return shop != null && perf != null;
    }
}
