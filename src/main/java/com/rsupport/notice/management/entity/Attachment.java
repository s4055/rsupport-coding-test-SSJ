package com.rsupport.notice.management.entity;

import com.rsupport.notice.management.enums.UseStatus;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "Attachment")
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long attachmentId;

  @Column(name = "file_name", nullable = false, length = 256)
  private String fileName;

  @Column(name = "file_path", nullable = false, length = 100)
  private String filePath;

  @Enumerated(EnumType.STRING)
  @Column(name = "is_use", nullable = false, length = 1)
  private UseStatus isUse = UseStatus.Y;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "notice_id")
  private Notice notice;

  public Attachment(String fileName, String filePath, Notice notice) {
    this.fileName = fileName;
    this.filePath = filePath;
    this.notice = notice;
  }
}
