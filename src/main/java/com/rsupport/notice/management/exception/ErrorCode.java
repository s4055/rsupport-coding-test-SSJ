package com.rsupport.notice.management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  OK(0, "성공", HttpStatus.OK),
  BAD_REQUEST(1, "잘못된 요청 데이터", HttpStatus.BAD_REQUEST),
  NO_SUCH_NOTICE(2, "해당 공지사항이 없습니다.", HttpStatus.NOT_FOUND),
  NO_SUCH_ATTACHMENT(3, "해당 첨부파일 정보가 없습니다.", HttpStatus.NOT_FOUND),
  ATTACHMENT_NOT_FOUND_IN_SERVER(4, "해당 첨부파일이 없습니다.", HttpStatus.NOT_FOUND),
  FILE_EXTENSION_FAIL(5, "파일 확장자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
  INTERNAL_SERVER_ERROR(9, "서버 에러", HttpStatus.INTERNAL_SERVER_ERROR);

  private final int resultCode;
  private final String message;
  private final HttpStatus httpStatus;
}
