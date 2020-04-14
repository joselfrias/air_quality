package com.josefrias.air_quality.model.apiModel;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Index {


    @SerializedName("display_name")
    private String name;

    @SerializedName("aqi")
    private Integer aqi;

    @SerializedName("aqi_display")
    private String aqiDisplay;

    @SerializedName("color")
    private String color;

    @SerializedName("category")
    private String category;

    @SerializedName("dominant_pollutant")
    private String dominantPollutant;

    public Index(String name, Integer aqi, String aqiDisplay, String color, String category, String dominantPollutant) {
        this.name = name;
        this.aqi = aqi;
        this.aqiDisplay = aqiDisplay;
        this.color = color;
        this.category = category;
        this.dominantPollutant = dominantPollutant;
    }

    public Index() {

    }

    public String getName() {
        return name;
    }


    public Integer getAqi() {
        return aqi;
    }


    public String getAqiDisplay() {
        return aqiDisplay;
    }


    public String getColor() {
        return color;
    }


    public String getCategory() {
        return category;
    }


    public String getDominantPollutant() {
        return dominantPollutant;
    }



    @Override
    public String toString() {
        return "{" +
                "\"name\""+":" +'\''+ name + '\'' +
                ","+"\"aqi\""+":" + aqi +
                ","+"\"aqi_display\""+":"+'\'' + aqiDisplay + '\'' +
                ", "+"\"color\""+":"+'\'' + color + '\'' +
                ", "+"\"category\""+":"+'\'' + category + '\'' +
                ", "+ "\"dominant_pollutant\""+":"+'\'' + dominantPollutant + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return Objects.equals(name, index.name) &&
                Objects.equals(aqi, index.aqi) &&
                Objects.equals(aqiDisplay, index.aqiDisplay) &&
                Objects.equals(color, index.color) &&
                Objects.equals(category, index.category) &&
                Objects.equals(dominantPollutant, index.dominantPollutant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, aqi, aqiDisplay, color, category, dominantPollutant);
    }
}
