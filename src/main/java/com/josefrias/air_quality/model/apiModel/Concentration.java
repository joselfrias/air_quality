package com.josefrias.air_quality.model.apiModel;

import com.google.gson.annotations.SerializedName;


public class Concentration {

    @SerializedName("value")
    private double value;

    @SerializedName("units")
    private String units;

    public Concentration(){

    }
    public Concentration(double value, String units) {
        this.value = value;
        this.units = units;
    }
    public double getValue() {
        return value;
    }
    public String getUnits() {
        return units;
    }

    @Override
    public String toString() {
        return "{" + "\"value\"" +":" + value + ","+
                "\"units\"" + ":" + '\''+ units + '\'' + "}";
    }
}
