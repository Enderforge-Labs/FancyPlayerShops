package com.snek.framework.utils.scheduler;

import org.jetbrains.annotations.NotNull;








/**
 * A class that lets you control scheduled loop tasks.
 * <p> Instances of this class are returned by the Scheduler's methods.
 */
public class LoopTaskHandler extends TaskHandler {
    private final long interval;
    public long getInterval() { return interval; }


    /**
     * Creates a new LoopTaskHandler.
     * @param _targetTick The tick the first iteration of this task is scheduled for, measures in ticks.
     * @param _interval The interval between iterations, measures in ticks.
     * @param _task The task to execute.
     */
    public LoopTaskHandler(final long _targetTick, final long _interval, final @NotNull Runnable _task) {
        super(_targetTick, _task);
        interval = _interval;
    }
}
