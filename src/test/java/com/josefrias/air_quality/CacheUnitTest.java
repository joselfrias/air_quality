package com.josefrias.air_quality;


import com.josefrias.air_quality.cache.Cache;
import com.josefrias.air_quality.cache.CacheObject;
import com.josefrias.air_quality.model.apiModel.Data;
import com.josefrias.air_quality.model.apiModel.Index;
import com.josefrias.air_quality.model.apiModel.IndexList;
import com.josefrias.air_quality.model.apiModel.ResponseData;
import com.josefrias.air_quality.model.CoordResponse;
import com.josefrias.air_quality.model.Coordinate;
import com.josefrias.air_quality.repository.AirQualityRepository;
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
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CacheUnitTest {

    @Mock(lenient = true)
    private AirQualityRepository airQualityRepository;


    @InjectMocks
    private Cache cache;

    @BeforeEach
    public void setUp() throws ParseException {
        Coordinate coordinate= new Coordinate(-40.0, -40.0);
        Index index=new Index("baqi", 70, "70", "#fff", "category1", "co");
        IndexList indexList= new IndexList(index);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse("2020-04-02 13:00:00");
        Data data=new Data(date, indexList);

        ResponseData responseData= new ResponseData(null, data, null);
        CacheObject cacheObject= new CacheObject(responseData, 10000);
        CoordResponse coord_response= new CoordResponse(coordinate, cacheObject);

        Mockito.when(airQualityRepository.findByCoordAndDate(coordinate.getLat(), coordinate.getLon(), 2020, 4, 2))
                .thenReturn(coord_response);

    }

    @Test
    public void whenExistingCoordinatesAndDate_thenResponseShouldBeFound() {
        double lat=-40.0;
        double lon=-40.0;
        CoordResponse found = cache.get(new Coordinate(lat, lon), "2020-04-02");

        assertThat(found.getC().getLat()).isEqualTo(lat);
        assertThat(found.getC().getLon()).isEqualTo(lon);
    }

    @Test
    public void whenValidCoordinates_thenResponseShouldExist() {
        boolean doesCoordsExist = cache.containsCoord(new Coordinate(-40.0, -40.0),"2020-04-02");
        assertThat(doesCoordsExist).isEqualTo(true);

        verifyFindByCoordAndDateIsCalledOnce(-40.0, -40.0, "2020-04-02");
    }

    @Test
    public void whenNotExistingDate_thenResponseShouldNotBeFound() {
        double lat=-40.0;
        double lon=-40.0;
        CoordResponse fromDb = cache.get(new Coordinate(lat, lon), "2019-04-02");
        assertThat(fromDb).isNull();

        verifyFindByCoordAndDateIsCalledOnce(lat, lon, "2019-04-02");
    }

    @Test
    public void whenNotExistingCoords_thenResponseShouldNotBeFound() {
        double lat=-20.0;
        double lon=-20.0;
        CoordResponse fromDb = cache.get(new Coordinate(lat, lon),"2020-04-02" );
        assertThat(fromDb).isNull();

        verifyFindByCoordAndDateIsCalledOnce(lat, lon, "2020-04-02");
    }


    @Test
    public void whenNonExistingCoordsAndDate_thenResponseShouldNotExist() {
        boolean doesCoordExist = cache.containsCoord(new Coordinate(-20.0, -40.0),"2019-04-02");
        assertThat(doesCoordExist).isEqualTo(false);

        verifyFindByCoordAndDateIsCalledOnce(-20.0, -40.0, "2019-04-02");
    }


    @Test
    public void test_statistics(){
        Cache.setMisses(1);
        Cache.setHits(1);
        Cache.setCountOfRequests(1);

        assertEquals(1,Cache.getMisses());
        assertEquals(1,Cache.getHits());
        assertEquals(1,Cache.getCountOfRequests());
    }


    private void verifyFindByCoordAndDateIsCalledOnce(double lat, double lon, String date) {
        String splitting[]=date.split("-");
        int year= Integer.parseInt(splitting[0]);
        int mont= Integer.parseInt(splitting[1]);
        int day=Integer.parseInt(splitting[2]);
        Mockito.verify(airQualityRepository, VerificationModeFactory.times(1)).findByCoordAndDate(lat, lon,year, mont, day );
        Mockito.reset(airQualityRepository);
    }

}
