package com.rsupport.notice.management.dto.common;

import com.rsupport.notice.management.entity.Notice;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class NoticeDto {
  private Long noticeId;
  private String title;
  private boolean hasAttachment;
  private LocalDateTime createDate;
  private Integer viewCount;
  private String author;

  public NoticeDto(Notice notice) {
    this.noticeId = notice.getNoticeId();
    this.title = notice.getTitle();
    this.hasAttachment = notice.getHasAttachment();
    this.createDate = notice.getCreateDate();
    this.viewCount = notice.getViewCount();
    this.author = notice.getAuthor();
  }
}
