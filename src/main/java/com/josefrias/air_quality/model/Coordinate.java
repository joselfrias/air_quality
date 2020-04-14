package com.josefrias.air_quality.model;


import javax.persistence.*;

import java.util.Objects;

@Embeddable
public class Coordinate {

    @Column(name="lat")
    private double lat;

    @Column(name="lon")
    private double lon;

    public Coordinate(){

    }

    public Coordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Double.compare(that.lat, lat) == 0 &&
                Double.compare(that.lon, lon) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }
}
