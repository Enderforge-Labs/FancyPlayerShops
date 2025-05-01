package com.snek.framework.data_types.containers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;








/**
 * A simple collection of three objects of different types.
 */
public class Triplet<F, S, T> {
    private @Nullable F first;
    private @Nullable S second;
    private @Nullable T third;

    // Getters
    public @Nullable F getFirst () { return first;  }
    public @Nullable S getSecond() { return second; }
    public @Nullable T getThird () { return third;  }

    // Setters
    public void setFirst (final @Nullable F _first ) { first  = _first;  }
    public void setSecond(final @Nullable S _second) { second = _second; }
    public void setThird (final @Nullable T _third ) { third  = _third;  }




    /**
     * Creates a new Triplet.
     * Both elements are set to null.
     */
    public Triplet() {
        first  = null;
        second = null;
        third  = null;
    }


    /**
     * Creates a new Triplet using the specified values.
     * @param _first The first value.
     * @param _second The second value.
     * @param _third The third value.
     */
    public Triplet(final @Nullable F _first, final @Nullable S _second, final @Nullable T _third) {
        first  = _first;
        second = _second;
        third  = _third;
    }


    /**
     * Creates a new Triplet using the specified values.
     * @param _first The first value.
     * @param _second The second value.
     * @param _third The third value.
     * @return The newly created Triplet.
     */
    public static <F, S, T> @NotNull Triplet<@Nullable F, @Nullable S, @Nullable T> from(final @Nullable F _first, final @Nullable S _second, final @Nullable T _third) {
        return new Triplet<>(_first, _second, _third);
    }
}
