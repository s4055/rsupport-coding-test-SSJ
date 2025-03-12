package com.rsupport.notice.management.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Attachment")
public class Attachment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long attachmentId;

  @Column(name = "file_name", nullable = false, length = 256)
  private String fileName;

  @Column(name = "file_path", nullable = false, length = 100)
  private String filePath;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "notice_id")
  private Notice notice;

  public Attachment(String fileName, String filePath, Notice notice) {
    this.fileName = fileName;
    this.filePath = filePath;
    this.notice = notice;
  }
}
