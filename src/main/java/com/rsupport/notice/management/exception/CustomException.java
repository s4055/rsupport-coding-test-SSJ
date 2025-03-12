package com.rsupport.notice.management.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends Exception {

  private final ErrorCode errorCode;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public int getErrorCode() {
    return errorCode.getResultCode();
  }

  public String getErrorMessage() {
    return errorCode.getMessage();
  }

  public HttpStatus getErrorHttpStatus() {
    return errorCode.getHttpStatus();
  }
}
