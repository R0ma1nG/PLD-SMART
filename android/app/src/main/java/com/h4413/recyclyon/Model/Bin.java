package com.h4413.recyclyon.Model;

public class Bin {

    private String _id;
    private String adress;
    private double lattitude;
    private double longitude;
    private boolean full;

    public Bin(String id, String adress, double lat, double aLong, boolean full) {
        this._id = id;
        this.adress = adress;
        lattitude = lat;
        longitude = aLong;
        this.full = full;
    }

    public String getId() {
        return _id;
    }

    public String getAdress() {
        return adress;
    }

    public double getLat() {
        return lattitude;
    }

    public double getLong() {
        return longitude;
    }

    public boolean isFull() {
        return full;
    }


}
