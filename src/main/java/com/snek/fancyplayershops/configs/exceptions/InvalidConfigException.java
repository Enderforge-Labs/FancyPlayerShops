package com.snek.fancyplayershops.configs.exceptions;

/**
 * Exception thrown when a configuration validation fails.
 * Provides detailed information about the configuration field and validation error.
 */
public class InvalidConfigException extends RuntimeException {

    /**
     * Creates a new InvalidConfigException with formatted message.
     *
     * @param configName the name of the configuration field that failed validation
     * @param message the validation error message
     */
    public InvalidConfigException(String configName, String message) {
        super(String.format("Invalid configuration for '%s': %s", configName, message));
    }
}
