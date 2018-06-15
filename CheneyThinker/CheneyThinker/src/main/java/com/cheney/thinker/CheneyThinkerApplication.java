package com.cheney.thinker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-16
 */
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