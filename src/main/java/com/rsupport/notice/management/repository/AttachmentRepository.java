package com.rsupport.notice.management.repository;

import com.rsupport.notice.management.entity.Attachment;
import com.rsupport.notice.management.enums.UseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

  @Modifying
  @Query("UPDATE Attachment a SET a.isUse = :useStatus WHERE a.notice.noticeId = :noticeId")
  void updateNoticeIsUse(@Param("noticeId") Long noticeId, @Param("useStatus") UseStatus useStatus);
}
