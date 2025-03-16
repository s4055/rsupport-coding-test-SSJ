package com.rsupport.notice.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("공지사항 관리 API")
                .description("공지사항 등록, 조회, 수정, 삭제 API 문서")
                .version("1.0"));
  }
}
