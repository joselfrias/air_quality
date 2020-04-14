package com.josefrias.air_quality.model.apiModel;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Embedded;
import java.util.Objects;

public class IndexList {

    @Embedded
    @SerializedName("baqi")
    public Index baqi;


    public IndexList(){

    }
    public IndexList(Index baqi) {
        this.baqi = baqi;
    }

    public Index getBaqi() {
        return baqi;
    }


    @Override
    public String toString() {
        return "{" +
                "\"baqi\"" +":" + baqi +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexList indexList = (IndexList) o;
        return Objects.equals(baqi, indexList.baqi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baqi);
    }
}
