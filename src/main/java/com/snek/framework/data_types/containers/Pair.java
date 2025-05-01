package com.snek.framework.data_types.containers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;








/**
 * A simple collection of two values of different types.
 */
public class Pair<F, S> {
    private @Nullable F first;
    private @Nullable S second;

    // Getters
    public @Nullable F getFirst () { return first;  }
    public @Nullable S getSecond() { return second; }

    // Setters
    public void setFirst (final @Nullable F _first ) { first  = _first;  }
    public void setSecond(final @Nullable S _second) { second = _second; }




    /**
     * Creates a new Pair.
     * Both elements are set to null.
     */
    public Pair() {
        first  = null;
        second = null;
    }


    /**
     * Creates a new Pair using the specified values.
     * @param _first The first value.
     * @param _second The second value.
     */
    public Pair(final @Nullable F _first, final @Nullable S _second) {
        first  = _first;
        second = _second;
    }


    /**
     * Creates a new Pair using the specified values.
     * @param _first The first value.
     * @param _second The second value.
     * @return The newly created Pair.
     */
    public static <K, V> @NotNull Pair<@Nullable K, @Nullable V> from(final @Nullable K first, final @Nullable V second) {
        return new Pair<>(first, second);
    }
}
