package com.h4413.recyclyon.Model;

public class Bin {

    private int id;
    private String adress;
    private double Lat;
    private double Long;
    private boolean full;

    public Bin(int id, String adress, double lat, double aLong, boolean full) {
        this.id = id;
        this.adress = adress;
        Lat = lat;
        Long = aLong;
        this.full = full;
    }

    public int getId() {
        return id;
    }

    public String getAdress() {
        return adress;
    }

    public double getLat() {
        return Lat;
    }

    public double getLong() {
        return Long;
    }

    public boolean isFull() {
        return full;
    }


}
