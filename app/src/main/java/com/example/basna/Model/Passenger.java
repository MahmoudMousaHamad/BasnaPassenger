package com.example.basna.Model;

public class Passenger {

    private double lat;
    private double lng;
    private String id;

    public Passenger()
    {

    }

    public Passenger(double _lat, double _lng, String _id)
    {
        lat = _lat;
        lng = _lng;
        id = _id;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public String getId()
    {
        return id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
