package com.weather.service;

import java.util.Map;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-25
 */
public interface WeatherService {

  Map<String, Object> index(String json) throws Exception;

}