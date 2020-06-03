package com.example.watchchecker.dataModel;

/**
 * An enum that stores each generic watch type, ie. "Diver", "Chronograph", "Dress" used for general
 * organization and the icon in the choose watch grid view
 */

public enum WatchType {
    DRESS("dress"),
    DIVER("diver"),
    AVIATION("aviation"),
    CHRONOGRAPH("chronograph"),
    HIGH_COMPLICATION("high_complication");

    private final String name;

    WatchType(String name) {
        this.name = name;
    }
}
