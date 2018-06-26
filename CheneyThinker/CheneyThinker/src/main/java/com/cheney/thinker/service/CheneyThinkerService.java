package com.cheney.thinker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-26
 */
@Service
public class CheneyThinkerService {

  @Autowired
  private RestTemplate restTemplate;

  public Map<String, Object> index(Map<String, Object> map) throws Exception {
    map.put("projectName", "CheneyThinker");
    map.put("version", "PRO");
    map.put("major", "1.0");
    return map;
  }

}