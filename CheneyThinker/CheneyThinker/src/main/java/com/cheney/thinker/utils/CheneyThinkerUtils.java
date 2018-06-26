package com.cheney.thinker.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.Base64;
import java.util.Map;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-26
 */
public final class CheneyThinkerUtils {

  private static ObjectMapper mapper = new ObjectMapper();

  public static Object invoke(Object data, Object service) throws Exception {
    Map<String, Object> map = (Map<String, Object>) data;
    Method method = service.getClass().getMethod((String) map.get("method"), Map.class);
    return method.invoke(service, map);
  }

  public static <T> T getEntity(String json, Class<T> clazz) throws Exception {
    return getEntity(json, clazz, false);
  }

  public static <T> T getEntityFromBase64(String json, Class<T> clazz) throws Exception {
    return getEntity(json, clazz, true);
  }

  public static Map<String, Object> getMap(String json) throws Exception {
    return getEntity(json, Map.class);
  }

  public static Map<String, Object> getMapFromBase64(String json) throws Exception {
    return getEntityFromBase64(json, Map.class);
  }

  private static <T> T getEntity(String json, Class<T> clazz, boolean fromBase64) throws Exception {
    if (StringUtils.isEmpty(json)) {
      throw new Exception("Argument Not Allowed Empty!");
    }
    if (fromBase64) {
      byte[] bytes = Base64.getDecoder().decode(json);
      json = new String(bytes, "UTF-8");
    }
    return mapper.readValue(json, clazz);
  }

  public static <T> T get(String url, RestTemplate restTemplate, Class<T> clazz) {
    return restTemplate.getForObject(url, clazz);
  }

  public static <T> T post(String url, Map<String, Object> params, RestTemplate restTemplate, Class<T> clazz) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
    map.setAll(params);
    HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(map, headers);
    return restTemplate.postForObject(url, formEntity , clazz);
  }

  public static <T> T postJson(String url, Map<String, Object> params, RestTemplate restTemplate, Class<T> clazz) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    try {
      return restTemplate.postForObject(url, new HttpEntity<String>(mapper.writeValueAsString(params), headers), clazz);
    } catch (Exception e) {
      //It's impossible to happen
      return null;
    }
  }

}