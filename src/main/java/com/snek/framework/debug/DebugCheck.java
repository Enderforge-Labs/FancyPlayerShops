package com.snek.framework.debug;




public abstract class DebugCheck {
    private DebugCheck() {}
    private static final boolean IS_DEBUG = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");

    public static boolean isDebug() {
        return IS_DEBUG;
    }
}
