package com.example.diet2.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

public final class ProfilePrefs {

    private static final String KEY_HEIGHT_CM = "profile_height_cm";
    private static final String KEY_WEIGHT_KG = "profile_weight_kg";
    private static final String KEY_PHOTO_URI = "profile_photo_uri";

    private ProfilePrefs() {
    }

    public static void setHeightCm(@NonNull Context context, @NonNull String heightCm) {
        prefs(context).edit().putString(KEY_HEIGHT_CM, heightCm).apply();
    }

    public static void setWeightKg(@NonNull Context context, @NonNull String weightKg) {
        prefs(context).edit().putString(KEY_WEIGHT_KG, weightKg).apply();
    }

    public static void setPhotoUri(@NonNull Context context, @Nullable String uri) {
        prefs(context).edit().putString(KEY_PHOTO_URI, uri).apply();
    }

    @NonNull
    public static String getHeightCm(@NonNull Context context) {
        String v = prefs(context).getString(KEY_HEIGHT_CM, "");
        return v == null ? "" : v;
    }

    @NonNull
    public static String getWeightKg(@NonNull Context context) {
        String v = prefs(context).getString(KEY_WEIGHT_KG, "");
        return v == null ? "" : v;
    }

    @Nullable
    public static String getPhotoUri(@NonNull Context context) {
        return prefs(context).getString(KEY_PHOTO_URI, null);
    }

    private static SharedPreferences prefs(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }
}
