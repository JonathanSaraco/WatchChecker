package com.example.watchchecker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.watchchecker.R;
import com.example.watchchecker.theme.ThemeConfiguration;

public class ThemeUtil {

    private static String INVALID_THEME_CONFIG_STRING = "Theme enum value is invalid.";

    private static ThemeConfiguration themeConfiguration = null;

    public static ThemeConfiguration getThemeFromPreferences(Context context) {
        if (themeConfiguration == null) {
            SharedPreferences themePreference = PreferenceManager.getDefaultSharedPreferences(context);
            themeConfiguration = ThemeConfiguration.getValueOf(themePreference.getString(context.getString(R.string.ref_theme_key), "light"));
        }
        return themeConfiguration;
    }

    public static void resetThemeConfiguration() {
        themeConfiguration = null;
    }

    public static int getThemeResourceID(ThemeConfiguration theme) {
        switch (theme) {
            case LIGHT:
                return R.style.LightAppTheme;
            case DARK:
                return R.style.DarkAppTheme;
            case AMOLED:
                return R.style.AmoledAppTheme;
        }
        throw new IllegalStateException(INVALID_THEME_CONFIG_STRING);
    }

    public static int getNoActionBarThemeResourceID(ThemeConfiguration theme) {
        switch (theme) {
            case LIGHT:
                return R.style.LightAppTheme_NoActionBar;
            case DARK:
                return R.style.DarkAppTheme_NoActionBar;
            case AMOLED:
                return R.style.AmoledAppTheme_NoActionBar;
        }
        throw new IllegalStateException(INVALID_THEME_CONFIG_STRING);
    }

    public static int getDialogThemeResourceID(ThemeConfiguration theme) {
        switch (theme) {
            case LIGHT:
                return R.style.LightAppTheme_Dialog;
            case DARK:
                return R.style.DarkAppTheme_Dialog;
            case AMOLED:
                return R.style.AmoledAppTheme_Dialog;
        }
        throw new IllegalStateException(INVALID_THEME_CONFIG_STRING);
    }

    public static int getWatchPlaceholderImageID(ThemeConfiguration theme) {
        switch (theme) {
            case LIGHT:
                return R.drawable.watch_placeholder_image;
            case DARK:
            case AMOLED:
                return R.drawable.night_watch_placeholder;
        }
        throw new IllegalStateException(INVALID_THEME_CONFIG_STRING);
    }
}
