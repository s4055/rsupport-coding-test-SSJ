package com.rsupport.notice.management.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class NoticeCreateRequest {
  @NotBlank(message = "작성자는 필수 입니다.")
  private String author;

  @NotBlank(message = "제목은 필수 입니다.")
  private String title;

  @NotBlank(message = "내용은 필수 입니다.")
  private String content;

  @NotNull(message = "공지 시작일시는 필수 입니다.")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime startDate;

  @NotNull(message = "공지 종료일시는 필수 입니다.")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime endDate;

  @AssertTrue(message = "공지 시작일시는 현재 일시 보다 이전일 수 없습니다.")
  public boolean isStartDateValid() {
    return startDate.isAfter(LocalDateTime.now());
  }

  @AssertTrue(message = "공지 종료일시는 공지 시작일시 보다 이전일 수 없습니다.")
  public boolean isEndDateValid() {
    return endDate.isAfter(startDate);
  }
}
