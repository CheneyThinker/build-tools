package com.cheney.thinker.core;

/**
 * @description 缁熶竴缁撴灉鍝嶅簲鐮乗n * @author CheneyThinker
 * @date 2018-06-16
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