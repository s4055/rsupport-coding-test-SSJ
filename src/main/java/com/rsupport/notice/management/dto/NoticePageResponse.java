package com.rsupport.notice.management.dto;

import com.rsupport.notice.management.entity.Notice;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class NoticePageResponse extends CommonResponse {

  private boolean isLast;
  private List<NoticePageDto> notices;

  public NoticePageResponse(int resultCode, String message, Page<Notice> notices) {
    super(resultCode, message);
    this.isLast = notices.isLast();
    this.notices = notices.stream().map(NoticePageDto::new).collect(Collectors.toList());
  }
}
