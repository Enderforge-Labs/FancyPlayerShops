package com.snek.framework.data_types.containers;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;








/**
 * A wrapper class that can track value changes of the contained object.
 */
public class Flagged<T> {
    private @Nullable T value;
    private boolean flag = true;


    /**
     * Creates a new Flagged value.
     * @param _value The initial value.
     */
    private Flagged(final @Nullable T _value) {
        this.value = _value;
    }

    /**
     * Creates a new Flagged value.
     * @param value The initial value.
     * @return The newly created Flagged object.
     */
    public static <T> Flagged<T> from(final @Nullable T value) {
        return new Flagged<>(value);
    }




    /**
     * Returns the current value without flagging the object.
     * @return The value.
     */
    public @Nullable T get() {
        return value;
    }


    /**
     * Sets a new value.
     * Flags the object if !this.get().equals(_value).
     * @param _value The new value.
     */
    public void set(final @Nullable T _value) {
        if(!Objects.equals(value, _value)) flag = true;
        value = _value;
    }


    /**
     * Flags the object and returns a reference to its value.
     * In case of immutable types, a copy is returned.
     * This method always flags the object without checking for changes.
     * @return The object.
     */
    public @Nullable T edit() {
        flag = true;
        return value;
    }




    /**
     * Returns the current value of the flag.
     * @return The flag.
     */
    public boolean isFlagged() {
        return flag;
    }


    /**
     * Unflags the Flagged object.
     */
    public void unflag() {
        flag = false;
    }
}
