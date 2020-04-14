package com.josefrias.air_quality.model.apiModel;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table(name="pollutant")
public class Pollutant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long pollutantId;

    @SerializedName("display_name")
    private String name;

    @SerializedName("full_name")
    private String fullName;

    @Embedded
    @SerializedName("concentration")
    private Concentration concentration;

    public Pollutant(){

    }

    public Pollutant(long pollutantId, String name, String fullName, Concentration concentration) {
        this.pollutantId = pollutantId;
        this.name = name;
        this.fullName = fullName;
        this.concentration = concentration;
    }

    public long getPollutantId() {
        return pollutantId;
    }


    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public Concentration getConcentration() {
        return concentration;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\"" + ":" +'\''+ name + '\''+ "," +
                "\"fullName\"" +":"+ '\''+ fullName + '\'' + "," +
                "\"concentration\""+ ":" + concentration +
                '}';
    }
}
