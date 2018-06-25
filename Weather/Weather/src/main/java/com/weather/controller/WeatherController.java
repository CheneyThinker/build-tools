package com.weather.controller;

import com.weather.core.Response;
import com.weather.core.ResponseGenerator;
import com.weather.mapping.WeatherMapping;
import com.weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-25
 */
@RestController
@RequestMapping("/Weather")
@CrossOrigin(maxAge = 3600, origins = "*")
public class WeatherController {

  @Autowired
  private WeatherService service;
  private Logger logger = LoggerFactory.getLogger(WeatherController.class);

  @RequestMapping("/index")
  public Response index(HttpServletRequest request) {
    try {
      return ResponseGenerator.genYes(service.index(request.getParameter("data")));
    } catch (Exception e) {
      return ResponseGenerator.genNo(logger, e, WeatherMapping.INDEX);
    }
  }

}