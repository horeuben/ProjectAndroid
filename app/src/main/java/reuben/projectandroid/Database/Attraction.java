package reuben.projectandroid.Database;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by reube on 15/11/2017.
 */

public class Attraction {
    private String name, description;
    private AttractionType type;
    private String placeid;

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AttractionType getType() {
        return type;
    }

    public void setType(AttractionType type) {
        this.type = type;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }

//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(name);
//        dest.writeString(description);
//    }
//    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
//    public static final Parcelable.Creator<Attraction> CREATOR = new Parcelable.Creator<Attraction>() {
//        public Attraction createFromParcel(Parcel in) {
//            return new Attraction(in);
//        }
//
//        public Attraction[] newArray(int size) {
//            return new Attraction[size];
//        }
//    };
//    private Attraction(Parcel in) {
//        name = in.readString();
//    }
    public enum AttractionType {
        HOTEL,
        LOCAL_MALL,
        LOCAL_SEE;
    }
}