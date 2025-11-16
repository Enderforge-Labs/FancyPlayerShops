package com.snek.fancyplayershops.configs;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworkconfig.ConfigManager;
import com.snek.fancyplayershops.main.FancyPlayerShops;








/**
 * A utility class that contains configuration file data.
 */
public abstract class Configs {
    private Configs() {}
    public static @NotNull ShopConfig shop = null;




    /**
     * Loads the configuration files or creates new ones if they are missing.
     */
    public static void loadConfigs() {
        shop = ConfigManager.loadConfig("ShopConfig",  ShopConfig.class, FancyPlayerShops.MOD_ID);
    }
}
