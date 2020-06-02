package com.example.watchchecker.dataModel;

import java.util.Date;

/**
 * Class that contains all data for a specific watch ie. its brand, model, serial number, movement,
 * its general type (ie. chronograph, dress, diver), and then its timing data which involves a
 * set of deltas from NIST time and the period over which the measurements took place
 */
public class WatchDataEntry {

    /**
     * The brand of the watch, ie. "Rolex", "Seiko", etc.
     */
    private final String brand;

    /**
     * The model of the watch, ie. "Submariner 16610", "Samurai SRPB99", etc.
     */
    private final String model;

    /**
     * The watch's movement, ie. "ETA-2842-2", "6R15C", etc.
     */
    private final String movement;

    /**
     * The generic type of the watch, ie "Diver", "Chronograph", etc.
     */
    private final WatchType watchType;

    /**
     * The date that the watch was purchased
     */
    private final Date purchaseDate;

    /**
     * The date of the watch's last service
     */
    private Date lastServiceDate;

    public WatchDataEntry(String brand,
                          String model,
                          String movement,
                          WatchType watchType,
                          Date purchaseDate,
                          Date lastServiceDate) {

        this.brand = brand;
        this.model = model;
        this.movement = movement;
        this.purchaseDate = purchaseDate;
        this.lastServiceDate = lastServiceDate;
        this.watchType = watchType;
    }

    /**
     * Get the brand string of the watch
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Get the model string of the watch
     */
    public String getModel() {
        return model;
    }

    /**
     * Get the {@link WatchType} of the watch
     */
    public WatchType getWatchType() {
        return watchType;
    }

    /**
     * Return the watch's movement string
     */
    public String getMovement() {
        return movement;
    }

    /**
     * Return the watch's purchase date
     */
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Return the watch's last service date
     */
    public Date getLastServiceDate() {
        return lastServiceDate;
    }

    /**
     * Set the watch's last service date
     */
    public void setLastServiceDate(Date lastServiceDate) {
        this.lastServiceDate = lastServiceDate;
    }
}
