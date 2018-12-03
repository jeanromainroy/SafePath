package com.safepath.safepath;

public class Hazard {

    // Attributes
    private double latitude_;
    private double longitude_;
    private String dateTime_;

    // Constructor
    public Hazard(double latitude, double longitude, String datetime){
        this.latitude_ = latitude;
        this.longitude_ = longitude;
        this.dateTime_ = datetime;
    }

    // Getter
    public double getLatitude(){
        return latitude_;
    }

    public double getLongitude(){
        return longitude_;
    }

    public String getDateTime() {
        return dateTime_;
    }

}
