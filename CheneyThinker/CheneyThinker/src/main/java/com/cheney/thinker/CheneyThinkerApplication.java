package com.cheney.thinker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-26
 */
@ServletComponentScan
@SpringBootApplication
public class CheneyThinkerApplication {

  public static void main(String[] args) {
    SpringApplication.run(CheneyThinkerApplication.class, args);
    StringBuilder builder = new StringBuilder("\n");
    builder.append("--------------------------------------------\n");
    builder.append("|                                          |\n");
    builder.append("|                                          |\n");
    builder.append("|   CheneyThinkerApplication is Running!   |\n");
    builder.append("|                                          |\n");
    builder.append("|                                          |\n");
    builder.append("--------------------------------------------\n");
    System.out.println(builder.toString());
  }

}