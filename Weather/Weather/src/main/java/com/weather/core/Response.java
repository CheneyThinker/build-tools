package com.weather.core;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @description Entity of Response
 * @author CheneyThinker
 * @date 2018-06-25
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

  private Integer code;
  private String msg;
  private Object data;

  public Integer getCode() {
    return code;
  }

  public Response setCode(ResponseCode code) {
    this.code = code.getCode();
    return this;
  }

  public String getMsg() {
    return msg;
  }

  public Response setMsg(String msg) {
    this.msg = msg;
    return this;
  }

  public Object getData() {
    return data;
  }

  public Response setData(Object data) {
    this.data = data;
    return this;
  }

}