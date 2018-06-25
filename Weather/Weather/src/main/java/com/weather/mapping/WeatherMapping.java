package com.weather.mapping;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-25
 */
public enum WeatherMapping {

  INDEX("Call index fail!");

  final String content;

  WeatherMapping(final String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

}
