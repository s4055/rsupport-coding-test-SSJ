package com.rsupport.notice.management.dto;

import com.rsupport.notice.management.entity.Notice;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NoticeSearchResponse extends CommonResponse {

  private boolean isLast;
  private List<NoticeDto> notices;

  public NoticeSearchResponse(int resultCode, String message, Page<Notice> notices) {
    super(resultCode, message);
    this.isLast = notices.isLast();
    this.notices = notices.stream().map(NoticeDto::new).collect(Collectors.toList());
  }
}
