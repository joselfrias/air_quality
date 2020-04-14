package com.josefrias.air_quality.model.apiModel;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Objects;

public class Data {

    @Temporal(TemporalType.TIMESTAMP)
    @SerializedName("datetime")
    @Column(name="datetime")
    private Date datetime;

    @Embedded
    @SerializedName("indexes")
    public IndexList indexes;

    @Embedded
    @SerializedName("pollutants")
    public PollutantList pollutants;


    @Embedded
    @SerializedName("health_recommendations")
    public HealthRecommendation healthRecommendations;



    public Data(){

    }

    public Data(Date datetime, IndexList indexes) {
        this.datetime = datetime;
        this.indexes = indexes;

    }

    public Data(Date datetime, IndexList indexes, HealthRecommendation healthRecommendations) {
        this.datetime = datetime;
        this.indexes = indexes;
        this.healthRecommendations = healthRecommendations;
    }
    public Data(Date datetime, IndexList indexes,PollutantList pollutants, HealthRecommendation healthRecommendations) {
        this.datetime = datetime;
        this.indexes = indexes;
        this.pollutants=pollutants;
        this.healthRecommendations = healthRecommendations;
    }

    public HealthRecommendation getHealthRecommendations() {
        return healthRecommendations;
    }


    public Date getDatetime() {
        return datetime;
    }


    public IndexList getIndexes() {
        return indexes;
    }



    @Override
    public String toString() {
        if (pollutants!=null){
            return "{"+"\"indexes\""+":" + indexes+ ","+
                    "\"pollutants\""+":" + pollutants + "}";
        }
        return "{"+"\"indexes\""+":" + indexes+ "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return Objects.equals(datetime, data.datetime) && Objects.equals(indexes, data.indexes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datetime,indexes);
    }
}
