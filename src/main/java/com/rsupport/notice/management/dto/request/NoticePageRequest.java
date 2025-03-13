package com.rsupport.notice.management.dto.request;

import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticePageRequest {
  @Min(value = 1, message = "단위는 1 이상 입니다.")
  private int size;

  @Min(value = 0, message = "페이지는 0 이상 입니다.")
  private int page;
}
