package com.rsupport.notice.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class NoticePageRequest {
  private int size;
  private int page;
}
