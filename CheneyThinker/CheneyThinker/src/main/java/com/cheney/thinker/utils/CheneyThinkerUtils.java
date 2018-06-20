package com.cheney.thinker.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-20
 */
public final class CheneyThinkerUtils {

  private static ObjectMapper mapper = new ObjectMapper();

  public static <T> T getEntity(String json, Class<T> clazz) throws Exception {
    return getEntity(json, clazz, false);
  }

  public static <T> T getEntityFromBase64(String json, Class<T> clazz) throws Exception {
    return getEntity(json, clazz, true);
  }

  public static Map<String, Object> getMap(String json) throws Exception {
    return getEntity(json, Map.class, false);
  }

  public static Map<String, Object> getMapFromBase64(String json) throws Exception {
    return getEntity(json, Map.class, true);
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
    return restTemplate.getForEntity(url, clazz).getBody();
  }

  public static <T> T post(String url, Map<String, Object> params, RestTemplate restTemplate, Class<T> clazz, boolean isSendJson) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(isSendJson ? MediaType.parseMediaType("application/json; charset=UTF-8") : MediaType.APPLICATION_FORM_URLENCODED);
    if (isSendJson) {
      try {
        String json = mapper.writeValueAsString(params);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<>(json, headers);
        return restTemplate.postForObject(url, formEntity, clazz);
      } catch (Exception e) {
        //It's impossible to happen
        return null;
      }
    } else {
      MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
      map.setAll(params);
      HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(map, headers);
      ResponseEntity<T> response = restTemplate.postForEntity(url, formEntity , clazz);
      return (200 == response.getStatusCodeValue() ? (response.hasBody() ? response.getBody() : null) : null);
    }
  }

}