package com.cheney.thinker.service.impl;

import com.cheney.thinker.service.CheneyThinkerService;
import com.cheney.thinker.utils.CheneyThinkerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-20
 */
@Service
public class CheneyThinkerServiceImpl implements CheneyThinkerService {

  @Autowired
  private RestTemplate restTemplate;
  public Map<String, Object> index(String json) throws Exception {
    Map<String, Object> map = CheneyThinkerUtils.getMapFromBase64(json);
    map.put("projectName", "CheneyThinker");
    map.put("version", "PRO");
    map.put("major", "1.0");
    return map;
  }

}