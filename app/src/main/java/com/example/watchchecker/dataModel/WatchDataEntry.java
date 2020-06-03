package com.example.watchchecker.dataModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that contains all data for a specific watch ie. its brand, model, serial number, movement,
 * its general type (ie. chronograph, dress, diver), and then its timing data which involves a
 * set of deltas from NIST time and the period over which the measurements took place
 */
public class WatchDataEntry implements Parcelable {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static final String PARCEL_KEY = "watchDataEntry";

    /**
     * The brand of the watch, ie. "Rolex", "Seiko", etc.
     */
    private String brand;

    /**
     * The model of the watch, ie. "Submariner 16610", "Samurai SRPB99", etc.
     */
    private String model;

    /**
     * The watch's movement, ie. "ETA-2842-2", "6R15C", etc.
     */
    private String movement;

    /**
     * The generic type of the watch, ie "Diver", "Chronograph", etc.
     */
    private WatchType watchType;

    /**
     * The date that the watch was purchased
     */
    private Date purchaseDate;

    /**
     * The date of the watch's last service
     */
    private Date lastServiceDate;

    /* NO ARGS CONSTRUCTOR FOR SERIALIZATION */
    private WatchDataEntry() {
        this("", "", "", WatchType.DIVER, new Date(), new Date());
    }

    public WatchDataEntry(String brand,
                          String model,
                          String movement,
                          WatchType watchType,
                          Date purchaseDate,
                          Date lastServiceDate) {
        this.brand = brand;
        this.model = model;
        this.movement = movement;
        this.watchType = watchType;
        this.purchaseDate = purchaseDate;
        this.lastServiceDate = lastServiceDate;
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

    /* Parcelable implementation stuff  */

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<WatchDataEntry> CREATOR = new Parcelable.Creator<WatchDataEntry>() {
        @Override
        public WatchDataEntry createFromParcel(Parcel source) {
            return new WatchDataEntry(source);
        }

        @Override
        public WatchDataEntry[] newArray(int size) {
            return new WatchDataEntry[size];
        }
    };

    private WatchDataEntry(Parcel parcel) {
        brand = parcel.readString();
        model = parcel.readString();
        movement = parcel.readString();
        watchType = WatchType.valueOf(parcel.readString());
        try {
            purchaseDate = DATE_FORMAT.parse(parcel.readString());
            lastServiceDate = DATE_FORMAT.parse(parcel.readString());
        } catch (ParseException e) {
            if (purchaseDate == null) {
                purchaseDate = new Date();
            }
            if (lastServiceDate == null) {
                lastServiceDate = new Date();
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brand);
        dest.writeString(model);
        dest.writeString(movement);
        dest.writeString(watchType.name());
        dest.writeString(DATE_FORMAT.format(purchaseDate));
        dest.writeString(DATE_FORMAT.format(lastServiceDate));
    }
}
