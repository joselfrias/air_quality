package com.josefrias.air_quality.model.apiModel;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.util.Objects;


public class ResponseData {

    @SerializedName("metadata")
    private String metadata;

    @Embedded
    @SerializedName("data")
    public Data data;

    @SerializedName("error")
    private String error;

    public ResponseData(){

    }
    public ResponseData(String metadata, Data data, String error) {
        this.metadata = metadata;
        this.data = data;
        this.error = error;
    }

    public String getMetadata() {
        return metadata;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseData that = (ResponseData) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadata, data, error);
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "data=" + data +
                '}';
    }
}
