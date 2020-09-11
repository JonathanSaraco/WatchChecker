package com.example.watchchecker.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.watchchecker.util.ImageUtil;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Date;
import java.util.Objects;

/**
 * Class that contains all data for a specific watch ie. its brand, model, serial number, movement,
 * its general type (ie. chronograph, dress, diver), and then its timing data which involves a
 * set of deltas from NIST time and the period over which the measurements took place
 */
public class WatchDataEntry implements Parcelable {

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
     * The date that the watch was purchased
     */
    private DateString purchaseDate;

    /**
     * The date of the watch's last service
     */
    private DateString lastServiceDate;

    /**
     * The date when this WatchDataEntry was created
     */
    private DateString creationDate;

    /**
     * The path of this watch's image
     */
    private String imagePath = "";

    /* NO ARGS CONSTRUCTOR FOR SERIALIZATION */
    private WatchDataEntry() {
        this("", "", "", new DateString(), new DateString());
    }

    public WatchDataEntry(String brand,
                          String model,
                          String movement,
                          DateString purchaseDate,
                          DateString lastServiceDate) {
        this.brand = brand;
        this.model = model;
        this.movement = movement;
        this.purchaseDate = purchaseDate;
        this.lastServiceDate = lastServiceDate;
        this.creationDate = new DateString(new Date());
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
     * Return the watch's movement string
     */
    public String getMovement() {
        return movement;
    }

    /**
     * Return the watch's purchase date
     */
    public DateString getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Return the watch's last service date
     */
    public DateString getLastServiceDate() {
        return lastServiceDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        if (!imagePath.equals(this.imagePath)) {
            // Delete the old image if it exists
            try {
                FileUtils.forceDelete(new File(this.imagePath));
                ImageUtil.removeBitmapFromMap(this.imagePath);
                Log.i("WatchDataEntry", String.format("Deleted %s's old image file successfully.", this.toDisplayString()));
            } catch (Exception ignore) {
            }
            this.imagePath = imagePath;
        }
    }

    /**
     * Set the watch's last service date
     */
    public void setLastServiceDate(DateString lastServiceDate) {
        this.lastServiceDate = lastServiceDate;
    }

    public DateString getCreationDate() {
        return creationDate;
    }

    /**
     * @return the string used as the display name for this entry.
     */
    public String toDisplayString() {
        if (!brand.isEmpty()) {
            return String.format("%s %s", brand, model);
        } else {
            return model;
        }
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
        purchaseDate = DateString.tryToParse(parcel.readString());
        lastServiceDate = DateString.tryToParse(parcel.readString());
        creationDate = DateString.tryToParse(parcel.readString());
        imagePath = parcel.readString();
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
        dest.writeString(purchaseDate.getComplexDateString());
        dest.writeString(lastServiceDate.getComplexDateString());
        dest.writeString(creationDate.getComplexDateString());
        dest.writeString(imagePath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WatchDataEntry that = (WatchDataEntry) o;
        return Objects.equals(getBrand(), that.getBrand()) &&
                Objects.equals(getModel(), that.getModel()) &&
                Objects.equals(getMovement(), that.getMovement()) &&
                Objects.equals(getPurchaseDate(), that.getPurchaseDate()) &&
                Objects.equals(getLastServiceDate(), that.getLastServiceDate()) &&
                Objects.equals(getCreationDate(), that.getCreationDate()) &&
                Objects.equals(getImagePath(), that.getImagePath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBrand(),
                getModel(),
                getMovement(),
                getPurchaseDate(),
                getLastServiceDate(),
                getCreationDate(),
                getImagePath());
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }

    public void setPurchaseDate(DateString purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
