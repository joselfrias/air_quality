package com.josefrias.air_quality;


import com.josefrias.air_quality.cache.Cache;
import com.josefrias.air_quality.cache.CacheObject;
import com.josefrias.air_quality.model.CoordResponse;
import com.josefrias.air_quality.model.apiModel.Data;
import com.josefrias.air_quality.model.apiModel.Index;
import com.josefrias.air_quality.model.apiModel.IndexList;
import com.josefrias.air_quality.model.apiModel.ResponseData;
import com.josefrias.air_quality.model.Coordinate;
import com.josefrias.air_quality.service.AirQualityServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AirQualityServiceImplTest {

    @Mock(lenient=true)
    private Cache cache;

    @InjectMocks
    private AirQualityServiceImpl airQualityService;

    @BeforeEach
    public void setUp() throws ParseException {
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

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String currentDate=dtf.format(now);
        Mockito.when(cache.get(coordinate, currentDate)).thenReturn(coord_response);
        Mockito.when(cache.containsCoord(new Coordinate(-40.0, -40.0),currentDate)).thenReturn(true);
        Mockito.when(cache.containsCoord(new Coordinate(-20.0, -20.0),currentDate )).thenReturn(false);
        Mockito.when(cache.get(coordinate, "2020-04-02")).thenReturn(coord_response);
        Mockito.when(cache.containsCoord(coordinate, "2020-04-02")).thenReturn(true);
        Mockito.when(cache.containsCoord(new Coordinate(-20.0, -40.0), "2020-04-02")).thenReturn(false);
        Mockito.when(cache.add(coordinate, responseData)).thenReturn(coord_response);
        Mockito.when(cache.containsCoord(coordinate, "2020-04-09")).thenReturn(false);
        Mockito.when(cache.getContent()).thenReturn(Arrays.asList(coord_response));

    }
    @AfterEach
    public void reset(){
        Mockito.reset(cache);
    }
    @Test
    public void whenExistingCoordinates_thenResponseShouldBeFound() throws ParseException {
        double lat=-40.0;
        double lon=-40.0;
        Index index=new Index("baqi", 70, "70", "#fff", "category1", "co");
        IndexList indexList= new IndexList(index);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse("2020-04-02 13:00:00");
        Data data=new Data(date, indexList);
        ResponseData responseData= new ResponseData(null, data, null);
        CoordResponse found = airQualityService.findByCoordinateAndDate(new Coordinate(lat, lon), "2020-04-02");
        assertThat(found.getR().getResponseData()).isEqualTo(responseData);
    }

    @Test
    public void whenInValidCoordinates_thenResponseShouldNotBeFound() {
        double lat=0.0;
        double lon=0.0;
        CoordResponse fromDb = airQualityService.findByCoordinateAndDate(new Coordinate(lat, lon),"2020-04-02");
        assertThat(fromDb).isNull();
        verifyCacheFindByCoordAndDateIsCalledOnce(lat, lon, "2020-04-02");
    }

    @Test
    public void whenValidCoordinates_thenResponseShouldExist() {
        boolean doesCoordExist = airQualityService.containsCoord(new Coordinate(-40.0, -40.0),"2020-04-02");
        assertThat(doesCoordExist).isEqualTo(true);

        verifyCacheContainsCoordIsCalledOnce(-40.0, -40.0, "2020-04-02");
    }

    @Test
    public void whenNonExistingCoords_thenResponseShouldNotExist() {
        boolean doesCoordExist = cache.containsCoord(new Coordinate(-20.0, -40.0),"2020-04-02");
        assertThat(doesCoordExist).isEqualTo(false);

        verifyCacheContainsCoordIsCalledOnce(-20.0, -40.0, "2020-04-02");
    }



    @Test
    public void getStatsHits(){
        Cache.setHits(1);
        int hits=airQualityService.getStatistics("hits");
        assertEquals(1, hits);
    }

    @Test
    public void getStatsMisses(){
        Cache.setMisses(1);
        int misses=airQualityService.getStatistics("misses");
        assertEquals(1, misses);
    }

    @Test
    public void getStatsCountRequests(){
        Cache.setCountOfRequests(1);
        int countOfRequests=airQualityService.getStatistics("count_of_requests");
        assertEquals(1, countOfRequests);
    }

    @Test
    public void getStatsInvalidRequest(){
        int countOfRequests=airQualityService.getStatistics("invalid_request");
        assertEquals(-1, countOfRequests);
    }

    @Test
    public void IfInCache_ThenReturnDataOfCache() throws ParseException {
        double lat=-40.0;
        double lon=-40.0;
        ResponseData responseData=airQualityService.getDataByDate(new Coordinate(lat, lon),"2020-04-02");
        Index index=new Index("baqi", 70, "70", "#fff", "category1", "co");
        IndexList indexList= new IndexList(index);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse("2020-04-02 13:00:00");
        Data data=new Data(date, indexList);
        ResponseData rD= new ResponseData(null, data, null);
        assertThat(responseData).isEqualTo(rD);
        verifyCacheContainsCoordIsCalledOnce(lat, lon, "2020-04-02");

    }



    @Test
    public void IfInCache_ThenReturnDataOfCacheV2() throws ParseException {
        double lat=-40.0;
        double lon=-40.0;
        ResponseData responseData=airQualityService.getData(new Coordinate(lat, lon));
        Index index=new Index("baqi", 70, "70", "#fff", "category1", "co");
        IndexList indexList= new IndexList(index);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse("2020-04-02 13:00:00");
        Data data=new Data(date, indexList);
        ResponseData rD= new ResponseData(null, data, null);
        assertThat(responseData).isEqualTo(rD);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String currentDate=dtf.format(now);
        verifyCacheContainsCoordIsCalledOnce(lat, lon, currentDate);
        verifyCacheFindByCoordAndDateIsCalledOnce(lat, lon, currentDate);

    }

    @Test
    public void given1Record_whengetAll_thenReturn1Record() throws ParseException {
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
        List<CoordResponse> allCacheRecords=cache.getContent();
        verifyGetContentIsCalledOnce();

    }

    private void verifyCacheFindByCoordAndDateIsCalledOnce(double lat, double lon,String date) {
        Mockito.verify(cache, VerificationModeFactory.times(1)).get(new Coordinate(lat, lon),date);
    }

    private void verifyCacheContainsCoordIsCalledOnce(double lat, double lon,String date) {
        Mockito.verify(cache, VerificationModeFactory.times(1)).containsCoord(new Coordinate(lat, lon),date);
    }

    private void verifyGetContentIsCalledOnce(){
        Mockito.verify(cache, VerificationModeFactory.times(1)).getContent();
        Mockito.reset();
    }

}
