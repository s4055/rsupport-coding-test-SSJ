package com.rsupport.notice.management.entity;

import com.rsupport.notice.management.dto.NoticeCreateRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Entity
@Table(name = "Notice")
public class Notice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long noticeId;

  @Column(name = "title", nullable = false, length = 30)
  private String title;

  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @CreationTimestamp
  @Column(name = "create_date", nullable = false)
  private LocalDateTime createDate;

  @Column(name = "view_count")
  private Integer viewCount = 0;

  @Column(name = "author", nullable = false, length = 50)
  private String author;

  @OneToMany(mappedBy = "notice")
  private List<Attachment> attachments;

  public Notice(NoticeCreateRequest request) {
    this.title = request.getTitle();
    this.content = request.getContent();
    this.startDate = request.getStartDate();
    this.endDate = request.getEndDate();
    this.author = request.getAuthor();
  }
}
