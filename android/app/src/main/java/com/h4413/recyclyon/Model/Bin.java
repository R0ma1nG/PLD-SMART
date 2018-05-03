package com.h4413.recyclyon.Model;

public class Bin {

    private String _id;
    private String adresse;
    private double lattitude;
    private double longitude;
    private int remplissage;

    public Bin(String id, String adresse, double lat, double aLong, int full) {
        this._id = id;
        this.adresse = adresse;
        lattitude = lat;
        longitude = aLong;
        this.remplissage = full;
    }

    public String getId() {
        return _id;
    }

    public String getAdress() {
        return adresse;
    }

    public double getLat() {
        return lattitude;
    }

    public double getLong() {
        return longitude;
    }

    public boolean isFull() {
        return (remplissage == 1);
    }


}
