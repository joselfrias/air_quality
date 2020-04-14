package com.josefrias.air_quality.service;

import com.josefrias.air_quality.model.apiModel.ResponseData;
import com.josefrias.air_quality.model.Coordinate;

public interface AirQualityService {
    ResponseData getData(Coordinate coordinate);
    int getStatistics(String statistic);
    ResponseData getDataByDate(Coordinate coordinate, String date);
}
