package com.snek.framework.utils.scheduler;

import java.util.Comparator;
import java.util.PriorityQueue;

import org.jetbrains.annotations.NotNull;








/**
 * A utility class that can store tasks and execute them after a specified delay.
 */
public abstract class Scheduler {
    private Scheduler() {}
    private static long tickNum = 0;
    private static final @NotNull PriorityQueue<@NotNull TaskHandler> taskQueue = new PriorityQueue<>(Comparator.comparingLong(e -> e.getTargetTick()));

    /**
     * Returns the current tick number.
     * @return The tick number.
     */
    public static long getTickNum() {
        return tickNum;
    }




    /*
     * The tick function of the scheduler.
     * <p> Must be called exactly one time at the end of every server tick.
     */
    public static void tick() {

        // For each task that has to be executed
        while(taskQueue.peek().getTargetTick() <= tickNum) {

            // Execute it
            final TaskHandler handler = taskQueue.poll();
            handler.compute();

            // Renew task handler if it's a LoopTaskHadler and has not been cancelled
            if(handler instanceof LoopTaskHandler h && !h.cancelled) {
                h.setTargetTick(tickNum + h.getInterval());
                taskQueue.add(h);
            }
        }

        tickNum++;
    }




    /**
     * Runs a task on the main thread every <interval> ticks after a specified delay.
     * @param delay The initial delay, expressed in server ticks.
     * @param interval The time interval between calls, expressed in server ticks.
     * @param task The task to run.
     * @return The handler of the newly created task schedule.
     */
    public static @NotNull LoopTaskHandler loop(final long delay, final long interval, final @NotNull Runnable task) {
        @NotNull LoopTaskHandler handler = new LoopTaskHandler(tickNum + delay, interval, task);
        taskQueue.add(handler);
        return handler;
    }




    /**
     * Runs a task on the main thread after a specified delay.
     * @param delay The delay, expressed in server ticks.
     * @param task The task to run.
     * @return The handler of the newly created task schedule.
     */
    public static @NotNull TaskHandler schedule(final long delay, final @NotNull Runnable task) {
        @NotNull TaskHandler handler = new TaskHandler(tickNum + delay, task);
        taskQueue.add(handler);
        return handler;
    }




    /**
     * Runs a task on the main thread at the end of the current tick. Equivalent to calling TaskHandler.schedule with delay 0.
     * @param task The task to run.
     * @return The handler of the newly created task schedule.
     */
    public static @NotNull TaskHandler run(final @NotNull Runnable task) {
        return schedule(0, task);
    }
}
