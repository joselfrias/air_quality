package com.josefrias.air_quality.cache;

import com.josefrias.air_quality.model.apiModel.ResponseData;

import javax.persistence.Column;
import javax.persistence.Embedded;

public class CacheObject {

    @Embedded
    private ResponseData responseData;

    @Column(name="expiretime")
    private long expireTime;

    public CacheObject(){

    }
    public CacheObject(ResponseData responseData, long expireTime){
        this.responseData=responseData;
        this.expireTime=expireTime;
    }


    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }


}
