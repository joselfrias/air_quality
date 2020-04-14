package com.josefrias.air_quality.repository;


import com.josefrias.air_quality.model.CoordResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface AirQualityRepository  extends JpaRepository<CoordResponse, Long> {

    @Modifying
    @Query(value ="delete from coord_response cd where cd.expiretime <= ?1", nativeQuery = true)
    void delete(long currentTime);

    @Query(value="select * from coord_response cd where cd.lat= ?1 and cd.lon= ?2 and year(cd.datetime)=?3 and month(cd.datetime)=?4 and day(cd.datetime)=?5", nativeQuery=true)
    CoordResponse findByCoordAndDate(double lat, double lon, int year, int month, int day);

}
