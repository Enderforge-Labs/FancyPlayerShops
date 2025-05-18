package com.snek.fancyplayershops.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snek.fancyplayershops.main.FancyPlayerShops;

import net.fabricmc.loader.api.FabricLoader;








public abstract class ConfigManager {
    private ConfigManager() {}
    private static final @NotNull Gson gson = new GsonBuilder().setPrettyPrinting().create();




    /**
     * Loads a configuration file from the mod's config directory.
     * <p> This MUST be called on ServerLifecycleEvents.SERVER_STARTING before anything else.
     * <p> Any errors during loading will cause the mod to not start.
     * @param configName The name of the file.
     * @param configClass The class of the config file data.
     * @return The config file instance.
     */
    public static <T extends ConfigFile> @NotNull T loadConfig(final @NotNull String configName, final @NotNull Class<T> configClass) {


        // Read file if it exists
        final Path configDir = FabricLoader.getInstance().getConfigDir().resolve(FancyPlayerShops.MOD_ID);
        final File configPath = configDir.resolve(configName).toFile();
        if(configPath.exists()) {
            try(FileReader reader = new FileReader(configPath)) {
                return new Gson().fromJson(reader, configClass);
            } catch (IOException e) {
                e.printStackTrace();
                FancyPlayerShops.flagFatal();
            }
        }

        // Create default config and save it if the config file is missing
        else {
            try {
                final T r = configClass.getDeclaredConstructor().newInstance();
                saveConfig(configName, r);
                return r;
            } catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
                FancyPlayerShops.flagFatal();
            }
        }


        // Not used
        return null;
    }




    public static void saveConfig(final @NotNull String configName, final @NotNull ConfigFile config) {

        final Path configDir = FabricLoader.getInstance().getConfigDir().resolve(FancyPlayerShops.MOD_ID);
        final File configPath = configDir.resolve(configName).toFile();
        try(FileWriter writer = new FileWriter(configPath)) {
            Files.createDirectories(configDir);
            gson.toJson(config, writer);
        } catch(IOException e) {
            e.printStackTrace();
            FancyPlayerShops.flagFatal();
        }
    }
}
