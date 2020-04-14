package com.josefrias.air_quality.controller;

import com.josefrias.air_quality.model.apiModel.ResponseData;
import com.josefrias.air_quality.model.Coordinate;
import com.josefrias.air_quality.service.AirQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/")
public class AirQualityController {

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
        return "{" + "\"data\""+":"+ data.getData().toString() +"}";
    }

    @GetMapping(value="/api/air_quality/{latitude},{longitude}/{date}")
    @ResponseBody
    public String getAirQualityByDate(@PathVariable("latitude") String lat,@PathVariable("longitude") String lon, @PathVariable("date") String date){
        Coordinate c = new Coordinate(Double.parseDouble(lat), Double.parseDouble(lon));
        ResponseData data=airQualityService.getDataByDate(c, date);
        return "{" + "\"data\""+":"+ data.getData().toString() +"}";
    }





}




