package com.rsupport.notice.management.dto;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NoticeUpdateRequest {
  @NotBlank private String title;
  @NotBlank private String content;
  @NotNull private LocalDate startDate;
  @NotNull private LocalDate endDate;
}
