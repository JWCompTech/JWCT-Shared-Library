package com.jwcomptech.shared.values;

import java.io.Serializable;

/**
 * Provides mutable access to a value.
 *
 * @param <T> the type to set and get
 * @param <V> the value of the object that implements
 *            this class to allow for method chaining
 * @since 0.0.1
 */
public interface Value<T, V> extends Comparable<T>, Serializable {
    /**
     * Returns the value.
     * @return the stored value
     */
    T get();
    /**
     * Sets the value.
     * @param value the value to store
     * @return this instance
     */
    V set(T value);
}
