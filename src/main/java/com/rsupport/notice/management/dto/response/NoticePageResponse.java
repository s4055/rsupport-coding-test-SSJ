package com.rsupport.notice.management.dto.response;

import com.rsupport.notice.management.dto.common.CommonResponse;
import com.rsupport.notice.management.dto.common.NoticeDto;
import com.rsupport.notice.management.entity.Notice;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class NoticePageResponse extends CommonResponse {

  private boolean endFlag;
  private List<NoticeDto> notices;

  public NoticePageResponse(int resultCode, String message, Page<Notice> notices) {
    super(resultCode, message);
    this.endFlag = notices.isLast();
    this.notices = notices.stream().map(NoticeDto::new).collect(Collectors.toList());
  }
}
