package com.weather.service.impl;

import com.weather.service.WeatherService;
import com.weather.utils.WeatherUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-25
 */
@Service
public class WeatherServiceImpl implements WeatherService {

  @Autowired
  private RestTemplate restTemplate;

  public Map<String, Object> index(String json) throws Exception {
    Map<String, Object> map = WeatherUtils.getMapFromBase64(json);
    map.put("projectName", "Weather");
    map.put("version", "PRO");
    map.put("major", "1.0");
    return map;
  }

}