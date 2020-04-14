package com.josefrias.air_quality.model.apiModel;

import com.google.gson.annotations.SerializedName;

public class HealthRecommendation {

    @SerializedName("general_population")
    private String generalPopulation;


    public HealthRecommendation(String generalPopulation) {
        this.generalPopulation = generalPopulation;
    }
    public HealthRecommendation(){

    }
    public String getGeneralPopulation() {
        return generalPopulation;
    }



}
