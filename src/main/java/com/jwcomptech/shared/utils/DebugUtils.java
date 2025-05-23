package com.jwcomptech.shared.utils;

import org.jetbrains.annotations.NotNull;

import java.security.KeyPair;

@SuppressWarnings({"UseOfSystemOutOrSystemErr", "unused"})
public final class DebugUtils {
    public static void print(final String str) { System.out.println(str); }

    public static void print(final Boolean str) { System.out.println(str); }

    public static void print(final Integer num) { System.out.println(num); }

    public static void print(final Long num) { System.out.println(num); }

    public static void print(final @NotNull KeyPair keyPair) {
        System.out.println("Private Key: " + keyPair.getPrivate());
        System.out.println("Public Key: " + keyPair.getPublic());
    }

    /** Prevents instantiation of this utility class. */
    private DebugUtils() { }
}
