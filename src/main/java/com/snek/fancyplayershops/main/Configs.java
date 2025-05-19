package com.snek.fancyplayershops.main;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.config.ConfigManager;
import com.snek.fancyplayershops.config.implementations.ShopConfig;








public abstract class Configs {
    private Configs() {}
    public static @NotNull ShopConfig shop = null;




    public static void readConfigs(){
        shop = ConfigManager.loadConfig("ShopConfig", ShopConfig.class);
    }
}
