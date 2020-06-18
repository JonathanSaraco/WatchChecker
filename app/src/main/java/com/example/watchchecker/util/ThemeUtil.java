package com.example.watchchecker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.watchchecker.R;
import com.example.watchchecker.theme.ThemeConfiguration;

public class ThemeUtil {

    public static ThemeConfiguration getThemeFromPreferences(Context context) {
        SharedPreferences themePreference = PreferenceManager.getDefaultSharedPreferences(context);
        return ThemeConfiguration.getValueOf(themePreference.getString(context.getString(R.string.ref_theme_key), "light"));
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
        throw new IllegalStateException("Theme enum value is invalid.");
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
        throw new IllegalStateException("Theme enum value is invalid.");
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
        throw new IllegalStateException("Theme enum value is invalid.");
    }

}
