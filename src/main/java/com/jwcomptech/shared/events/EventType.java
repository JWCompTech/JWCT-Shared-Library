package com.jwcomptech.shared.events;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents a specific event type associated with an {@link Event}.
 * <p>
 * Event types form a hierarchy with the {@link EventType#ROOT} (equals to
 * {@link Event#ANY}) as its root. This is useful in event handler
 * registration where a single event handler can be registered to a
 * super event type and will be receiving its subtype events as well.
 * Note that you cannot construct two different EventType objects with the same
 * name and parent.
 * @param <T> the event class to which this type applies
 * @since 1.4.0
 */
@SuppressWarnings("unused")
public final class EventType<T extends Event> {
    /**
     * The root event type. All other event types are either direct or
     * indirect subtypes of it. It is also the only event type which
     * has its super event type set to {@code null}.
     */
    public static final EventType<Event> ROOT = new EventType<>("EVENT", null);

    @SuppressWarnings("FieldNotUsedInToString")
    private Set<EventType<? extends T>> subTypes;

    @SuppressWarnings("FieldNotUsedInToString")
    private final EventType<? super T> superType;

    private final String name;

    /**
     * Constructs a new {@code EventType} with the specified name and the
     * {@code EventType.ROOT} as its super type.
     * @param name the name
     * @throws IllegalArgumentException if an EventType with the same name and
     * {@link EventType#ROOT}/{@link Event#ANY} as parent
     */
    public EventType(final String name) { this(ROOT, name); }

    /**
     * Constructs a new {@code EventType} with the specified super type and
     * the name set to {@code null}.
     * @param superType the event super type
     * @throws IllegalArgumentException if an EventType with "null" name and
     * under this supertype exists
     */
    public EventType(final EventType<? super T> superType) { this(superType, null); }

    /**
     * Constructs a new {@code EventType} with the specified super type and name.
     * @param superType the event super type
     * @param name the name
     * @throws IllegalArgumentException if an EventType with the same name and
     * superType exists
     */
    public EventType(final EventType<? super T> superType,
                     final String name) {
        if (null == superType) throw new IllegalArgumentException("Event super type must not be null!");
        this.superType = superType;
        this.name = name;
        superType.createSubType(this);
    }

    /**
     * Internal constructor that skips various checks.
     * @param name the name
     * @param superType the event super type
     */
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    EventType(final String name,
              final EventType<? super T> superType) {
        this.superType = superType;
        this.name = name;
        if (null != superType) {
            if (null != superType.subTypes) {
                superType.subTypes
                        .removeIf(t -> Objects.equals(name, t.name));
            }
            superType.createSubType(this);
        }
    }

    /**
     * Gets the super type of this event type. The returned value is
     * {@code null} only for the {@code EventType.ROOT}.
     * @return the super type
     */
    public EventType<? super T> getSuperType() { return superType; }

    /**
     * Gets the name of this event type.
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Returns a string representation of this {@code EventType} object.
     * @return a string representation of this {@code EventType} object.
     */
    @Override
    public String toString() { return (null != name) ? name : super.toString(); }

    @Contract("_ -> new")
    public @NotNull EventType<T> createSubType(final String name) { return new EventType<>(this, name); }

    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    private void createSubType(final EventType<? extends T> subType) {
        if (null == subType) throw new IllegalArgumentException("Event super type must not be null!");
        if (null == subTypes) subTypes = new HashSet<>();
        subTypes.parallelStream().filter(t -> (Objects.equals(t.name, subType.name)))
                .forEach(t -> {
                    throw new IllegalArgumentException("EventType \"" + subType + '"'
                            + "with parent \"" + subType.superType + "\" already exists");
                });
        subTypes.add(subType);
    }
}