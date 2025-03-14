package com.rsupport.notice.management.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
public class LoggingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
      chain.doFilter(request, response);
      return;
    }

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);

    long startTime = System.currentTimeMillis();

    String requestBody = getRequestBody(requestWrapper);

    log.info("[Request] {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
    printHeaders(httpRequest);
    printParameters(httpRequest);
    printSessionInfo(httpRequest);
    log.info("[Body] {}", requestBody);

    chain.doFilter(requestWrapper, responseWrapper);

    String responseBody = getResponseBody(responseWrapper);

    long duration = System.currentTimeMillis() - startTime;

    log.info(
        "[Response] {} {} | Status: {} | Time: {}ms",
        httpRequest.getMethod(),
        httpRequest.getRequestURI(),
        httpResponse.getStatus(),
        duration);
    log.info("[Body] {}", responseBody);

    responseWrapper.copyBodyToResponse();
  }

  private String getRequestBody(ContentCachingRequestWrapper request) {
    byte[] content = request.getContentAsByteArray();
    return content.length > 0 ? new String(content, StandardCharsets.UTF_8) : "EMPTY";
  }

  private String getResponseBody(ContentCachingResponseWrapper response) throws IOException {
    byte[] content = response.getContentAsByteArray();
    return content.length > 0 ? new String(content, StandardCharsets.UTF_8) : "EMPTY";
  }

  private void printHeaders(HttpServletRequest request) {
    Enumeration<String> headerNames = request.getHeaderNames();

    if (headerNames == null) return;

    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      log.info("[Headers][{}]={}", headerName, request.getHeader(headerName));
    }
  }

  private void printParameters(HttpServletRequest request) {
    Map<String, String[]> parameterMap = request.getParameterMap();
    for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
      for (String value : entry.getValue()) {
        log.info("[Params][{}]={}", entry.getKey(), value);
      }
    }
  }

  private void printSessionInfo(HttpServletRequest request) {
    log.info(
        "[SessionID]={}",
        request.getSession(false) == null ? "NO_SESSION" : request.getSession().getId());
  }
}
