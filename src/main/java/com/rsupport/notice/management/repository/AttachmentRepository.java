package com.rsupport.notice.management.repository;

import com.rsupport.notice.management.entity.Attachment;
import com.rsupport.notice.management.enums.UseStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
  List<Attachment> findByNotice_noticeId(@Param("noticeId") Long noticeId);

  @Modifying
  @Query("DELETE FROM Attachment a WHERE a.notice.noticeId = :noticeId")
  void deleteByFiles(@Param("noticeId") Long noticeId);
}
