package com.josefrias.air_quality.cache;

import com.josefrias.air_quality.model.CoordResponse;
import com.josefrias.air_quality.model.apiModel.ResponseData;
import com.josefrias.air_quality.model.Coordinate;
import com.josefrias.air_quality.repository.AirQualityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class Cache {
    private static final int CLEAN_UP_PERIOD_IN_SEC =20 ;
    private static final int TIME_TO_LIVE =45;
    private static int countOfRequests=0;
    private static int hits=0;
    private static int misses=0;

    @Autowired
    private AirQualityRepository airQualityRepository;

    public Cache(){
        Thread cleanerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep((long)CLEAN_UP_PERIOD_IN_SEC * 1000);
                    airQualityRepository.delete(System.currentTimeMillis());

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    public CoordResponse add(Coordinate c, ResponseData r){
        setCountOfRequests(countOfRequests+1);
        long expireTime=System.currentTimeMillis()+TIME_TO_LIVE*1000;
        return airQualityRepository.save(new CoordResponse(c,new CacheObject(r,expireTime)));
    }


    public CoordResponse get(Coordinate c, String date){
        String[] splitting=date.split("-");
        int year= Integer.parseInt(splitting[0]);
        int mont= Integer.parseInt(splitting[1]);
        int day=Integer.parseInt(splitting[2]);
        return airQualityRepository.findByCoordAndDate(c.getLat(), c.getLon(), year, mont, day);
    }

    public static int getCountOfRequests() {
        return countOfRequests;
    }

    public static int getHits() {
        return hits;
    }


    public static int getMisses() {
        return misses;
    }



    public boolean containsCoord(Coordinate key, String date){
        String[] splitting=date.split("-");
        int year= Integer.parseInt(splitting[0]);
        int mont= Integer.parseInt(splitting[1]);
        int day=Integer.parseInt(splitting[2]);
        setCountOfRequests(countOfRequests+1);
        if (airQualityRepository.findByCoordAndDate(key.getLat(), key.getLon(), year, mont, day)!=null){
            setHits(hits+1);
            return true;
        }
        else{
            setMisses(misses+1);
            return false;
        }


    }

    public static void setCountOfRequests(int countOfRequests) {
        Cache.countOfRequests = countOfRequests;
    }

    public static void setHits(int hits) {
        Cache.hits = hits;
    }

    public static void setMisses(int misses) {
        Cache.misses = misses;
    }


}
