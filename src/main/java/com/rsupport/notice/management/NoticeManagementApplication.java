package com.rsupport.notice.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NoticeManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(NoticeManagementApplication.class, args);
  }
}
