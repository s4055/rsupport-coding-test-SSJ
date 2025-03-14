package com.rsupport.notice.management.exception;

import com.rsupport.notice.management.dto.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.rsupport.notice.management")
public class GlobalExceptionHandler {

  // 커스텀 예외
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<CommonResponse> handleCustomException(CustomException e) {
    printRestControllerException(e);
    CommonResponse errorResponse = new CommonResponse(e.getErrorCode(), e.getErrorMessage());
    return ResponseEntity.status(e.getErrorHttpStatus()).body(errorResponse);
  }

  // 유효성 검사 예외
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CommonResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    printRestControllerException(e);
    ErrorCode errorCode = ErrorCode.BAD_REQUEST;
    CommonResponse errorResponse =
        new CommonResponse(errorCode.getResultCode(), errorCode.getMessage());
    return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
  }

  // 바인딩 예외
  @ExceptionHandler(BindException.class)
  public ResponseEntity<CommonResponse> handleBindException(BindException e) {
    printRestControllerException(e);
    ErrorCode errorCode = ErrorCode.BAD_REQUEST;
    CommonResponse errorResponse =
        new CommonResponse(errorCode.getResultCode(), errorCode.getMessage());
    return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
  }

  // 나머지 예외
  @ExceptionHandler(Exception.class)
  public ResponseEntity<CommonResponse> handleGlobalException(Exception e) {
    printRestControllerException(e);
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    CommonResponse errorResponse =
        new CommonResponse(errorCode.getResultCode(), errorCode.getMessage());
    return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
  }

  private void printRestControllerException(Exception e) {
    log.error("Exception Occurred ", e);
  }
}
