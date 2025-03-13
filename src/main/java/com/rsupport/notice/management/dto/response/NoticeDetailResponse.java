package com.rsupport.notice.management.dto.response;

import com.rsupport.notice.management.dto.common.AttachmentDto;
import com.rsupport.notice.management.dto.common.CommonResponse;
import com.rsupport.notice.management.entity.Notice;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeDetailResponse extends CommonResponse {

  private String title;
  private String content;
  private String createDate;
  private Integer viewCount;
  private String author;
  private List<AttachmentDto> attachments;

  public NoticeDetailResponse(int resultCode, String message, Notice notice, int redisViewCount) {
    super(resultCode, message);
    this.title = notice.getTitle();
    this.content = notice.getContent();
    this.createDate = notice.getCreateDateToString();
    this.viewCount = notice.getViewCount() + redisViewCount;
    this.author = notice.getAuthor();
    this.attachments =
        notice.getAttachments().stream().map(AttachmentDto::new).collect(Collectors.toList());
  }
}
