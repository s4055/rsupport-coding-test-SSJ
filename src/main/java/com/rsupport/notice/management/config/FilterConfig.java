package com.rsupport.notice.management.config;

import com.rsupport.notice.management.filter.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  @Bean
  public FilterRegistrationBean<LoggingFilter> loggingFilter() {
    FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(new LoggingFilter());
    registrationBean.addUrlPatterns("/api/*");
    registrationBean.setOrder(1);

    return registrationBean;
  }
}
