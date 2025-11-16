package com.snek.fancyplayershops.main;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.config.old.ConfigManager;
import com.snek.fancyplayershops.config.old.implementations.ShopConfig;








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
        shop = ConfigManager.loadConfig("ShopConfig",  ShopConfig.class);
    }
}
