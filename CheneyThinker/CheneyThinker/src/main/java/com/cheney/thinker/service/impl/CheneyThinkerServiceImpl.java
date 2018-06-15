package com.cheney.thinker.service.impl;

import com.cheney.thinker.service.CheneyThinkerService;
import com.cheney.thinker.utils.CheneyThinkerUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-16
 */
@Service
public class CheneyThinkerServiceImpl implements CheneyThinkerService {

  public Map<String, Object> index(String json) throws Exception {
    Map<String, Object> map = CheneyThinkerUtils.getMapFromBase64(json);
    map.put("projectName", "CheneyThinker");
    map.put("version", "1.0");
    map.put("model", "PRO");
    return map;
  }

}