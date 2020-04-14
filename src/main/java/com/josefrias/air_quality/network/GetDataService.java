package com.josefrias.air_quality.network;

import com.josefrias.air_quality.model.apiModel.ResponseData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {
    @GET("current-conditions/")
    Call<ResponseData> getAirQuality(@Query("lat") double lat, @Query("lon") double lon, @Query("key") String key, @Query("features") String features);

    @GET("forecast/hourly/")
    Call<ResponseData> getAirQualityByDate(@Query("lat") double lat, @Query("lon") double lon, @Query("key") String key,@Query("datetime")String datetime);

}
