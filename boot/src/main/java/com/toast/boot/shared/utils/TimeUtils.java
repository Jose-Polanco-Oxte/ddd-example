/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.utils;

public final class TimeUtils {
    private TimeUtils() {
        // Private constructor to prevent instantiation
    }
    
    public static long secondsToMillis(long seconds) {
        return seconds * 1000;
    }
    
    public static long minutesToMillis(long minutes) {
        return minutes * 60 * 1000;
    }
    
    public static long daysToMillis(long days) {
        return days * 24 * 60 * 60 * 1000;
    }
}
