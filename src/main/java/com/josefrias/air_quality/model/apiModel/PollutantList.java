package com.josefrias.air_quality.model.apiModel;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

public class PollutantList {

    @OneToOne(cascade = CascadeType.ALL)
    @AttributeOverride(name = "pollutantId", column = @Column(name = "coID"))
    @JoinColumn(name = "coID")
    @SerializedName("co")
    private Pollutant co;

    @OneToOne(cascade = CascadeType.ALL)
    @AttributeOverride(name = "pollutantId", column = @Column(name = "no2ID"))
    @JoinColumn(name = "no2ID")
    @SerializedName("no2")
    private Pollutant no2;

    @OneToOne(cascade = CascadeType.ALL)
    @AttributeOverride(name = "pollutantId", column = @Column(name = "o3ID"))
    @JoinColumn(name = "o3ID")
    @SerializedName("o3")
    private Pollutant o3;

    @OneToOne(cascade = CascadeType.ALL)
    @AttributeOverride(name = "pollutantId", column = @Column(name = "pm10ID"))
    @JoinColumn(name = "pm10ID")
    @SerializedName("pm10")
    private Pollutant pm10;

    @OneToOne(cascade = CascadeType.ALL)
    @AttributeOverride(name = "pollutantId", column = @Column(name = "pm25ID"))
    @JoinColumn(name = "pm25ID")
    @SerializedName("pm25")
    private Pollutant pm25;

    @OneToOne(cascade = CascadeType.ALL)
    @AttributeOverride(name = "pollutantId", column = @Column(name = "so2ID"))
    @JoinColumn(name = "so2ID")
    @SerializedName("so2")
    private Pollutant so2;

    public PollutantList(){

    }
    public PollutantList(Pollutant co, Pollutant no2, Pollutant o3, Pollutant pm10, Pollutant pm25, Pollutant so2) {
        this.co = co;
        this.no2 = no2;
        this.o3 = o3;
        this.pm10 = pm10;
        this.pm25 = pm25;
        this.so2 = so2;
    }

    public Pollutant getCo() {
        return co;
    }

    public Pollutant getNo2() {
        return no2;
    }

    public Pollutant getO3() {
        return o3;
    }

    public Pollutant getPm10() {
        return pm10;
    }

    public Pollutant getPm25() {
        return pm25;
    }

    public Pollutant getSo2() {
        return so2;
    }

    @Override
    public String toString() {
        return "{" +
                "\"co\"" + ":" + co +","+
                "\"no2\"" + ":" + no2 +","+
                "\"o3\"" +":" + o3 + ","+
                "\"pm10\"" + ":" + pm10 + ","+
                "\"pm25\"" +":" + pm25 + ","+
                "\"so2\"" +":" + so2 +
                '}';
    }
}
