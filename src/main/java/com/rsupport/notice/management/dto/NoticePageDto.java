package com.rsupport.notice.management.dto;

import com.rsupport.notice.management.entity.Notice;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class NoticePageDto {
  private String title;
  private String isAttachment;
  private LocalDateTime createDate;
  private Integer viewCount;
  private String author;

  public NoticePageDto(Notice notice) {
    this.title = notice.getTitle();
    this.isAttachment = notice.getAttachments().isEmpty() ? "N" : "Y";
    this.createDate = notice.getCreateDate();
    this.viewCount = notice.getViewCount();
    this.author = notice.getAuthor();
  }
}
