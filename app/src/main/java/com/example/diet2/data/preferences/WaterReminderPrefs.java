package com.example.diet2.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

public final class WaterReminderPrefs {

    private static final String KEY_ENABLED = "water_reminder_enabled";
    private static final String KEY_INTERVAL_MINUTES = "water_reminder_interval_minutes";

    private WaterReminderPrefs() {
    }

    public static boolean isEnabled(@NonNull Context context) {
        return prefs(context).getBoolean(KEY_ENABLED, false);
    }

    public static void setEnabled(@NonNull Context context, boolean enabled) {
        prefs(context).edit().putBoolean(KEY_ENABLED, enabled).apply();
    }

    public static int getIntervalMinutes(@NonNull Context context) {
        return prefs(context).getInt(KEY_INTERVAL_MINUTES, 60);
    }

    public static void setIntervalMinutes(@NonNull Context context, int minutes) {
        prefs(context).edit().putInt(KEY_INTERVAL_MINUTES, minutes).apply();
    }

    private static SharedPreferences prefs(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }
}
