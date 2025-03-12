package com.rsupport.notice.management.dto;

import com.rsupport.notice.management.entity.Notice;
import com.rsupport.notice.management.enums.UseStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class NoticeDetailResponse extends CommonResponse {

  private String title;
  private String content;
  private LocalDateTime createDate;
  private Integer viewCount;
  private String author;
  private List<AttachmentDto> attachments;

  public NoticeDetailResponse(int resultCode, String message, Notice notice) {
    super(resultCode, message);
    this.title = notice.getTitle();
    this.content = notice.getContent();
    this.createDate = notice.getCreateDate();
    this.viewCount = notice.getViewCount();
    this.author = notice.getAuthor();
    this.attachments =
        notice.getAttachments().stream()
            .filter(it -> it.getIsUse().equals(UseStatus.Y))
            .map(AttachmentDto::new)
            .collect(Collectors.toList());
  }
}
