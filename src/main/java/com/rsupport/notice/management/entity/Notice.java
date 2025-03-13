package com.rsupport.notice.management.entity;

import com.rsupport.notice.management.dto.NoticeCreateRequest;
import com.rsupport.notice.management.dto.NoticeUpdateRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Entity
@Table(name = "Notice")
@AllArgsConstructor
@NoArgsConstructor
public class Notice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long noticeId;

  @Column(name = "title", nullable = false, length = 30)
  private String title;

  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "start_date", nullable = false)
  private LocalDateTime startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDateTime endDate;

  @CreationTimestamp
  @Column(name = "create_date", nullable = false)
  private LocalDateTime createDate;

  @ColumnDefault("0")
  @Column(name = "view_count")
  private Integer viewCount = 0;

  @Column(name = "author", nullable = false, length = 50)
  private String author;

  @ColumnDefault("false")
  @Column(name = "has_attachment", nullable = false)
  private Boolean hasAttachment = false;

  @OneToMany(mappedBy = "notice")
  private List<Attachment> attachments = new ArrayList<>();

  public void incrementViewCount(int count) {
    this.viewCount += count;
  }

  public Notice(NoticeCreateRequest request, boolean hasAttachment) {
    this.title = request.getTitle();
    this.content = request.getContent();
    this.startDate = request.getStartDate();
    this.endDate = request.getEndDate();
    this.createDate = LocalDateTime.now();
    this.author = request.getAuthor();
    this.hasAttachment = hasAttachment;
  }

  public void updateNotice(
      NoticeUpdateRequest request, boolean hasAttachment, boolean isExistAttachments) {
    this.title = request.getTitle();
    this.content = request.getContent();
    this.startDate = request.getStartDate();
    this.endDate = request.getEndDate();
    this.hasAttachment = isExistAttachments || hasAttachment;
  }
}
