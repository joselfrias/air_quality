package com.josefrias.air_quality;


import com.josefrias.air_quality.cache.Cache;
import com.josefrias.air_quality.cache.CacheObject;
import com.josefrias.air_quality.model.CoordResponse;
import com.josefrias.air_quality.model.Coordinate;
import com.josefrias.air_quality.model.apiModel.Data;
import com.josefrias.air_quality.model.apiModel.Index;
import com.josefrias.air_quality.model.apiModel.IndexList;
import com.josefrias.air_quality.model.apiModel.ResponseData;
import com.josefrias.air_quality.repository.AirQualityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AirQualityApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ControllerITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AirQualityRepository airQualityRepository;

    @AfterEach
    public void resetDb() {
        airQualityRepository.deleteAll();
    }
    @Test
    public void givenAirQualityData_whenGetAirQualityData_thenStatus200() throws Exception {
        createTestAirQualityData();
        Index index=new Index("baqi", 70, "70", "#fff", "category1", "co");
        mvc.perform(get("/api/air_quality/-40.0,-40.0/2020-04-02").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.indexes.baqi.name", is(index.getName())))
                .andExpect(jsonPath("$.data.indexes.baqi.aqi", is(index.getAqi())))
                .andExpect(jsonPath("$.data.indexes.baqi.aqi_display", is(index.getAqiDisplay())))
                .andExpect(jsonPath("$.data.indexes.baqi.color", is(index.getColor())))
                .andExpect(jsonPath("$.data.indexes.baqi.category", is(index.getCategory())))
                .andExpect(jsonPath("$.data.indexes.baqi.dominant_pollutant", is(index.getDominantPollutant())));
    }

    private void createTestAirQualityData() throws ParseException {
        Coordinate coordinate= new Coordinate(-40.0, -40.0);
        Index index=new Index("baqi", 70, "70", "#fff", "category1", "co");
        IndexList indexList= new IndexList(index);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse("2020-04-02 13:00:00");
        Data data=new Data(date,
                indexList);
        ResponseData responseData= new ResponseData(null, data, null);
        CacheObject cacheObject= new CacheObject(responseData, 10000);
        CoordResponse coord_response= new CoordResponse(coordinate, cacheObject);
        airQualityRepository.saveAndFlush(coord_response);
    }








}
