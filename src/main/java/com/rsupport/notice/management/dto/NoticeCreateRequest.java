package com.rsupport.notice.management.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
public class NoticeCreateRequest {
  @NotBlank private String author;
  @NotBlank private String title;
  @NotBlank private String content;
  @NotNull private LocalDate startDate;
  @NotNull private LocalDate endDate;
}
