package com.snek.framework.debug;




/**
 * Utility class that can check if the mod is currently being ran in debug mode.
 */
public abstract class DebugCheck {
    private DebugCheck() {}
    private static final boolean IS_DEBUG = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");

    /**
     * Checks if the mod is in debug mode.
     * @return True if it's in debug mode. false otherwise.
     */
    public static boolean isDebug() {
        return IS_DEBUG;
    }
}
