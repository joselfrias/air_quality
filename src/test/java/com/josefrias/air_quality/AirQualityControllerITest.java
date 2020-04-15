package com.josefrias.air_quality;


import com.josefrias.air_quality.cache.CacheObject;
import com.josefrias.air_quality.controller.AirQualityController;
import com.josefrias.air_quality.model.CoordResponse;
import com.josefrias.air_quality.model.apiModel.Data;
import com.josefrias.air_quality.model.apiModel.Index;
import com.josefrias.air_quality.model.apiModel.IndexList;
import com.josefrias.air_quality.model.apiModel.ResponseData;
import com.josefrias.air_quality.model.Coordinate;
import com.josefrias.air_quality.service.AirQualityService;
import com.josefrias.air_quality.service.AirQualityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebMvcTest(AirQualityController.class)
public class AirQualityControllerITest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AirQualityServiceImpl service;


    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void givenLatAndLonAndDate_whenData_thenReturnData() throws Exception {
        Coordinate coordinate= new Coordinate(-40.0, -40.0);
        Index index=new Index("baqi", 70, "70", "#fff", "category1", "co");
        IndexList indexList= new IndexList(index);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse("2020-04-04 13:00:00");
        Data data=new Data(date,indexList);
        ResponseData responseData= new ResponseData(null, data, null);
        CacheObject cacheObject= new CacheObject(responseData, 10000);
        CoordResponse coord_response= new CoordResponse(coordinate, cacheObject);
        given(service.getDataByDate(coordinate,"2020-04-04")).willReturn(responseData);
        mvc.perform(get("/api/air_quality/-40.0,-40.0/2020-04-04").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.indexes.baqi.name", is(index.getName())))
                .andExpect(jsonPath("$.data.indexes.baqi.aqi", is(index.getAqi())))
        .andExpect(jsonPath("$.data.indexes.baqi.aqi_display", is(index.getAqiDisplay())))
        .andExpect(jsonPath("$.data.indexes.baqi.color", is(index.getColor())))
                .andExpect(jsonPath("$.data.indexes.baqi.category", is(index.getCategory())))
                .andExpect(jsonPath("$.data.indexes.baqi.dominant_pollutant", is(index.getDominantPollutant())));

        verify(service, VerificationModeFactory.times(1)).getDataByDate(coordinate,"2020-04-04");
        reset(service);
    }

    @Test
    public void givenLatAndLon_whenData_thenReturnData() throws Exception {
        Coordinate coordinate= new Coordinate(-40.0, -40.0);
        Index index=new Index("baqi", 70, "70", "#fff", "category1", "co");
        IndexList indexList= new IndexList(index);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse("2020-04-04 13:00:00");
        Data data=new Data(date,indexList);

        ResponseData responseData= new ResponseData(null, data, null);
        CacheObject cacheObject= new CacheObject(responseData, 10000);
        CoordResponse coord_response= new CoordResponse(coordinate, cacheObject);

        given(service.getData(coordinate)).willReturn(responseData);

        mvc.perform(get("/api/air_quality/now/-40.0,-40.0").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.indexes.baqi.name", is(index.getName())))
                .andExpect(jsonPath("$.data.indexes.baqi.aqi", is(index.getAqi())))
                .andExpect(jsonPath("$.data.indexes.baqi.aqi_display", is(index.getAqiDisplay())))
                .andExpect(jsonPath("$.data.indexes.baqi.color", is(index.getColor())))
                .andExpect(jsonPath("$.data.indexes.baqi.category", is(index.getCategory())))
                .andExpect(jsonPath("$.data.indexes.baqi.dominant_pollutant", is(index.getDominantPollutant())));

        verify(service, VerificationModeFactory.times(1)).getData(coordinate);
        reset(service);

    }

    @Test
    public void given1Record_whenGetAllRecords_thenReturnJsonArray() throws Exception {
        Coordinate coordinate= new Coordinate(-40.0, -40.0);
        Index index=new Index("baqi", 70, "70", "#fff", "category1", "co");
        IndexList indexList= new IndexList(index);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse("2020-04-04 13:00:00");
        Data data=new Data(date,indexList);

        ResponseData responseData= new ResponseData(null, data, null);
        CacheObject cacheObject= new CacheObject(responseData, 10000);
        CoordResponse coord_response= new CoordResponse(coordinate, cacheObject);

        List<CoordResponse> allRecords = Arrays.asList(coord_response);

        given(service.getCacheContent()).willReturn(allRecords);

        mvc.perform(get("/api/cache").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.cache", hasSize(1)))
                .andExpect(jsonPath("$.cache[0].data.indexes.baqi.name", is(index.getName())))
                .andExpect(jsonPath("$.cache[0].data.indexes.baqi.aqi", is(index.getAqi())))
                .andExpect(jsonPath("$.cache[0].data.indexes.baqi.aqi_display", is(index.getAqiDisplay())))
                .andExpect(jsonPath("$.cache[0].data.indexes.baqi.color", is(index.getColor())))
                .andExpect(jsonPath("$.cache[0].data.indexes.baqi.category", is(index.getCategory())))
                .andExpect(jsonPath("$.cache[0].data.indexes.baqi.dominant_pollutant", is(index.getDominantPollutant())));
        verify(service, VerificationModeFactory.times(1)).getCacheContent();
        reset(service);
    }

    @Test
    public void givenHits_WhenGetHits_THenReturnHits() throws Exception {
        given(service.getStatistics("hits")).willReturn(1);
        mvc.perform(get("/api/cache/hits").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hits", is(1)));
    }

    @Test
    public void givenMisses_WhenGetMisses_THenReturnMisses() throws Exception {
        given(service.getStatistics("misses")).willReturn(1);
        mvc.perform(get("/api/cache/misses").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.misses", is(1)));
    }

    @Test
    public void givenCount_WhenGetCount_THenReturnCount() throws Exception {
        given(service.getStatistics("count_of_requests")).willReturn(1);
        mvc.perform(get("/api/cache/count_of_requests").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_of_requests", is(1)));
    }




}
