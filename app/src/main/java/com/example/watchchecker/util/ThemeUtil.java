package com.example.watchchecker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.watchchecker.R;
import com.example.watchchecker.theme.ThemeConfiguration;

public class ThemeUtil {

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
            case DARK:
                return R.style.DarkAppTheme;
            case AMOLED:
                return R.style.AmoledAppTheme;
            case LIGHT:
            default:
                return R.style.LightAppTheme;
        }
    }

    public static int getNoActionBarThemeResourceID(ThemeConfiguration theme) {
        switch (theme) {
            case DARK:
                return R.style.DarkAppTheme_NoActionBar;
            case AMOLED:
                return R.style.AmoledAppTheme_NoActionBar;
            case LIGHT:
            default:
                return R.style.LightAppTheme_NoActionBar;
        }
    }

    public static int getDialogThemeResourceID(ThemeConfiguration theme) {
        switch (theme) {
            case DARK:
                return R.style.DarkAppTheme_Dialog;
            case AMOLED:
                return R.style.AmoledAppTheme_Dialog;
            case LIGHT:
            default:
                return R.style.LightAppTheme_Dialog;

        }
    }

    public static int getWatchPlaceholderImageID(ThemeConfiguration theme) {
        switch (theme) {
            case DARK:
            case AMOLED:
            case LIGHT:
            default:
                return R.drawable.ic_placeholder_watch;
        }
    }
}
