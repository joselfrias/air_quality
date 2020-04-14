package com.josefrias.air_quality.service;

import com.josefrias.air_quality.cache.Cache;
import com.josefrias.air_quality.model.*;
import com.josefrias.air_quality.model.apiModel.ResponseData;
import com.josefrias.air_quality.network.GetDataService;
import com.josefrias.air_quality.network.RetrofitClientInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class AirQualityServiceImpl implements AirQualityService{

    @Autowired
    Cache cache;


    public CoordResponse findByCoordinateAndDate(Coordinate c, String date){
        return cache.get(c, date);
    }

    public boolean containsCoord(Coordinate c, String date){
        return cache.containsCoord(c, date);
    }

    public CoordResponse addToCache(Coordinate c, ResponseData rd){
         return cache.add(c, rd);
    }

    public ResponseData getDataByDate(Coordinate coordinate, String date){
        if (containsCoord(coordinate,date)){
            return findByCoordinateAndDate(coordinate, date).getR().getResponseData();
        }

        return callApiByDate(coordinate, date);
    }

    public ResponseData getData(Coordinate coordinate){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String currentDate=dtf.format(now);
        if (containsCoord(coordinate,currentDate)){
            return findByCoordinateAndDate(coordinate, currentDate).getR().getResponseData();
        }
        return callApi(coordinate);

    }

    public ResponseData callApiByDate(Coordinate coordinate, String date){
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        String key = "1a0dec8c93b54932a6d3da1ce17d44bd";
        String transformedDate=date+"T16:00:00";
        Call<ResponseData> call = service.getAirQualityByDate(coordinate.getLat(), coordinate.getLon(), key, transformedDate);
        Response<ResponseData> apiResponse;
        try {
            apiResponse = call.execute();
            addToCache(coordinate, apiResponse.body());
            return apiResponse.body();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseData callApi(Coordinate coordinate){
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        String key = "1a0dec8c93b54932a6d3da1ce17d44bd";
        String features="breezometer_aqi,health_recommendations,pollutants_concentrations";
        Call<ResponseData> call = service.getAirQuality(coordinate.getLat(), coordinate.getLon(), key, features);
        Response<ResponseData> apiResponse;
        try {
            apiResponse = call.execute();
            addToCache(coordinate, apiResponse.body());
            return apiResponse.body();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getStatistics(String statistic){
        if (statistic.equals("hits")){
            return Cache.getHits();
        }
        else if (statistic.equals("misses")){
            return Cache.getMisses();
        }
        else if (statistic.equals("count_of_requests")){
            return Cache.getCountOfRequests();
        }
        else{
            return -1;
        }
    }





}
