package com.example.diet2.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

public final class OnboardingPrefs {

    private static final String KEY_COMPLETED = "onboarding_completed";

    private OnboardingPrefs() {
    }

    public static boolean isCompleted(@NonNull Context context) {
        return prefs(context).getBoolean(KEY_COMPLETED, false);
    }

    public static void setCompleted(@NonNull Context context, boolean completed) {
        prefs(context).edit().putBoolean(KEY_COMPLETED, completed).apply();
    }

    private static SharedPreferences prefs(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }
}
