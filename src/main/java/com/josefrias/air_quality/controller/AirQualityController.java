package com.josefrias.air_quality.controller;

import com.josefrias.air_quality.model.CoordResponse;
import com.josefrias.air_quality.model.apiModel.ResponseData;
import com.josefrias.air_quality.model.Coordinate;
import com.josefrias.air_quality.service.AirQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/")
public class AirQualityController {

    private static final String DATA="\"data\"";
    @Autowired
    private AirQualityService airQualityService;

    @GetMapping(path="/")
    public String searchForm(Model model){
        model.addAttribute("coordinate", new Coordinate());
        return "search_page";
    }

    @GetMapping(value = "/search")
    public String search(@RequestParam(value = "lat") String lat, @RequestParam(value = "lon") String lon, Model model) {

     		
        Coordinate c = new Coordinate(Double.parseDouble(lat), Double.parseDouble(lon));
        ResponseData responseData = airQualityService.getData(c);
        if (responseData==null){
            return "error_page";
        }
        model.addAttribute("responseData", responseData);
        model.addAttribute("lat", lat);
        model.addAttribute("lon", lon);
        return "data_page";
      
        }

    @GetMapping(value ="/api/cache/{statistic}")
    @ResponseBody
    public String getCacheStatistics(@PathVariable("statistic") String statistic){
        return "{" + statistic + ":" + airQualityService.getStatistics(statistic) + "}";
    }

    @GetMapping(value="api/air_quality/now/{latitude},{longitude}")
    @ResponseBody
    public String getNowAirQuality(@PathVariable("latitude") String lat, @PathVariable("longitude") String lon){
        Coordinate c = new Coordinate(Double.parseDouble(lat), Double.parseDouble(lon));
        ResponseData data=airQualityService.getData(c);
        if (data==null){
            return "{" + DATA+":"+ "\"\"" + "}";
        }
        return "{" + DATA+":"+ data.getData().toString() +"}";
    }

    @GetMapping(value="/api/air_quality/{latitude},{longitude}/{date}")
    @ResponseBody
    public String getAirQualityByDate(@PathVariable("latitude") String lat,@PathVariable("longitude") String lon, @PathVariable("date") String date){
        Coordinate c = new Coordinate(Double.parseDouble(lat), Double.parseDouble(lon));
        ResponseData data=airQualityService.getDataByDate(c, date);
        if (data==null){
            return "{" + DATA+":"+ "\"\"" + "}";
        }
        return "{" + DATA+":"+ data.getData().toString() +"}";
    }

    @GetMapping(value="/api/cache")
    @ResponseBody
    public String getAirQualityInCache(){
        List<CoordResponse> cacheContent=airQualityService.getCacheContent();
        if (cacheContent==null){
            return "{" + "\"cache\""+":"+ "\"\"" + "}";
        }
        return "{" + "\"cache\""+":"+ cacheContent.toString() + "}";
    }





}




