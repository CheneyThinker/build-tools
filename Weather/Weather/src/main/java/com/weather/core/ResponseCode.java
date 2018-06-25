package com.weather.core;

/**
 * @description Code of Response
 * @author CheneyThinker
 * @date 2018-06-25
 */
public enum ResponseCode {

  YES(200),
  NO(404);

  private final Integer code;

  ResponseCode(final Integer code) {
    this.code = code;
  }

  public Integer getCode() {
    return code;
  }

}