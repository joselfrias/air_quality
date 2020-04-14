package com.josefrias.air_quality.model;

import com.josefrias.air_quality.cache.CacheObject;


import javax.persistence.*;

@Entity
@Table(name = "coord_response")
public class CoordResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Embedded
    private Coordinate c;

    @Embedded
    private CacheObject r;

    public CoordResponse(){

    }
    public CoordResponse(Coordinate c, CacheObject r) {
        this.c = c;
        this.r = r;
    }

    public Coordinate getC() {
        return c;
    }


    public CacheObject getR() {
        return r;
    }


}
