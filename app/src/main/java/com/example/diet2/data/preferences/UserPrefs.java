package com.example.diet2.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

public final class UserPrefs {

    private static final String KEY_REGISTRATION_EPOCH_MS = "registration_epoch_ms";

    private UserPrefs() {
    }

    /**
     * Registration timestamp used for UI constraints (e.g., charts calendar).
     *
     * If not set yet, callers may treat it as "now".
     */
    public static long getRegistrationEpochMs(@NonNull Context context) {
        return prefs(context).getLong(KEY_REGISTRATION_EPOCH_MS, 0L);
    }

    public static void setRegistrationEpochMsIfAbsent(@NonNull Context context, long epochMs) {
        SharedPreferences p = prefs(context);
        if (p.getLong(KEY_REGISTRATION_EPOCH_MS, 0L) != 0L) return;
        p.edit().putLong(KEY_REGISTRATION_EPOCH_MS, epochMs).apply();
    }

    public static void setRegistrationEpochMs(@NonNull Context context, long epochMs) {
        prefs(context).edit().putLong(KEY_REGISTRATION_EPOCH_MS, epochMs).apply();
    }

    private static SharedPreferences prefs(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }
}
