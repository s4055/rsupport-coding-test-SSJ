package com.rsupport.notice.management.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    OK(0, "성공", HttpStatus.OK),
    BAD_REQUEST(1, "잘못된 요청 데이터", HttpStatus.BAD_REQUEST),
    NO_SUCH_NOTICE(2, "해당 공지사항이 없음", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR(9, "서버 에러", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int resultCode;
    private final String message;
    private final HttpStatus httpStatus;
}