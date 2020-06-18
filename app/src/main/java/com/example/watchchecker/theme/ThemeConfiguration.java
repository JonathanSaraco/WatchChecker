package com.example.watchchecker.theme;

public enum ThemeConfiguration {
    LIGHT("light"),
    DARK("dark"),
    AMOLED("amoled");

    private final String name;

    ThemeConfiguration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ThemeConfiguration getValueOf(String name) {
        if (LIGHT.name.equals(name)) {
            return LIGHT;
        } else if (DARK.name.equals(name)) {
            return DARK;
        } else if (AMOLED.name.equals(name)) {
            return AMOLED;
        }
        throw new IllegalStateException("ThemeConfiguration not implemented.");
    }
}
