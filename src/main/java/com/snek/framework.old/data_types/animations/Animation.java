package com.snek.framework.old.data_types.animations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;








/**
 * This class identifies an animation expressed as a list of Transitions.
 */
public class Animation {
    private final @NotNull List<@NotNull Transition> transitions = new ArrayList<>();
    private int totalDuration = 0;


    /**
     * Creates a new Animation.
     * @param _transitions One or more transitions.
     */
    public Animation(final @NotNull Transition... _transitions) {
        for(final Transition t : _transitions) {
            transitions.add(t);
            totalDuration += t.getDuration();
        }
    }


    /**
     * Returns the list of transitions that make up this animation as a read-only list.
     * @return The translations.
     */
    public @NotNull List<@NotNull Transition> getTransitions() {
        return Collections.unmodifiableList(transitions);
    }


    /**
     * Returns the total duration of this animation, expressed in ticks.
     * @return The total duration.
     */
    public int getTotalDuration() {
        return totalDuration;
    }
}
