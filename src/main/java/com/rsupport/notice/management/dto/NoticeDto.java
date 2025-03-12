package com.rsupport.notice.management.dto;

import com.rsupport.notice.management.entity.Notice;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class NoticeDto {
  private Long noticeId;
  private String title;
  private String isAttachment;
  private LocalDate createDate;
  private Integer viewCount;
  private String author;

  public NoticeDto(Notice notice) {
    this.noticeId = notice.getNoticeId();
    this.title = notice.getTitle();
    this.isAttachment = notice.getAttachments().isEmpty() ? "N" : "Y";
    this.createDate = notice.getCreateDate();
    this.viewCount = notice.getViewCount();
    this.author = notice.getAuthor();
  }
}
