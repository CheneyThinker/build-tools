package com.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description
 * @author CheneyThinker
 * @date 2018-06-25
 */
@SpringBootApplication
public class WeatherApplication {

  public static void main(String[] args) {
    SpringApplication.run(WeatherApplication.class, args);
    StringBuilder builder = new StringBuilder("\n");
    builder.append("--------------------------------------\n");
    builder.append("|                                    |\n");
    builder.append("|                                    |\n");
    builder.append("|   WeatherApplication is Running!   |\n");
    builder.append("|                                    |\n");
    builder.append("|                                    |\n");
    builder.append("--------------------------------------\n");
    System.out.println(builder.toString());
  }

}