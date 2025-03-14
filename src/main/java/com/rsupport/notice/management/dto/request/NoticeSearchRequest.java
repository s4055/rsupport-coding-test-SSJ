package com.rsupport.notice.management.dto.request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@AllArgsConstructor
public class NoticeSearchRequest {
  @Min(value = 1) // 제목 + 내용
  @Max(value = 2) // 제목
  private int searchType;

  @Size(min = 2, message = "최소 2 글자 이상 입니다.")
  @NotBlank(message = "검색어는 필수 입니다.")
  private String keyword;

  @NotNull(message = "시작일자는 필수 입니다.")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @NotNull(message = "종료일자는 필수 입니다.")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  @Min(value = 1, message = "단위는 1 이상 입니다.")
  private int size;

  @Min(value = 0, message = "페이지는 0 이상 입니다.")
  private int page;

  @AssertTrue(message = "종료일자는 시작일자 보다 이전일 수 없습니다.")
  public boolean isEndDateValid() {
    return endDate.isAfter(startDate);
  }

  public String getFormattedStartDate() {
    return startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
  }

  public String getFormattedEndDate() {
    return endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
  }
}
