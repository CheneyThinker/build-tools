package com.cheney.thinker.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Map;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-16
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

}