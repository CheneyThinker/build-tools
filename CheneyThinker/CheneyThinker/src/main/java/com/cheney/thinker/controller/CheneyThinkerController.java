package com.cheney.thinker.controller;

import com.cheney.thinker.core.Response;
import com.cheney.thinker.core.ResponseGenerator;
import com.cheney.thinker.mapping.CheneyThinkerMapping;
import com.cheney.thinker.service.CheneyThinkerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-16
 */
@RestController
@RequestMapping("/CheneyThinker")
@CrossOrigin(maxAge = 3600, origins = "*")
public class CheneyThinkerController {

  @Autowired
  private CheneyThinkerService service;
  private Logger logger = LoggerFactory.getLogger(CheneyThinkerController.class);

  @RequestMapping("/index")
  public Response index(HttpServletRequest request) {
    try {
      return ResponseGenerator.genYes(service.index(request.getParameter("data")));
    } catch (Exception e) {
      return ResponseGenerator.genNo(logger, e, CheneyThinkerMapping.INDEX);
    }
  }

}