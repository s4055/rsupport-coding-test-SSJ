package com.rsupport.notice.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticePageRequest {
  private int size;
  private int page;
}
