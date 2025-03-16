package com.rsupport.notice.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class SwaggerConfig {

  /**
   * Swagger 정보 설정
   *
   * @return the open api
   */
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("공지사항 관리 API")
                .description("공지사항 등록, 조회, 수정, 삭제 API 문서")
                .version("1.0"));
  }

  /**
   * Swagger 통한 DTO + MultipartFile 요청에 대한 예외 처리
   *
   * @param converter the converter
   */
  public SwaggerConfig(MappingJackson2HttpMessageConverter converter) {
    List<MediaType> supportMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
    supportMediaTypes.add(new MediaType("application", "octet-stream"));
    converter.setSupportedMediaTypes(supportMediaTypes);
  }
}
