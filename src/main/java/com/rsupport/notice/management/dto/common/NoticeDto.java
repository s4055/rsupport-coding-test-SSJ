package com.rsupport.notice.management.dto.common;

import com.rsupport.notice.management.entity.Notice;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeDto {
  private Long noticeId;
  private String title;
  private boolean hasAttachment;
  private String createDate;
  private Integer viewCount;
  private String author;

  public NoticeDto(Notice notice) {
    this.noticeId = notice.getNoticeId();
    this.title = notice.getTitle();
    this.hasAttachment = notice.getHasAttachment();
    this.createDate = notice.getCreateDateToString();
    this.viewCount = notice.getViewCount();
    this.author = notice.getAuthor();
  }
}
