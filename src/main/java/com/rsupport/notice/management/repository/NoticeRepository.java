package com.rsupport.notice.management.repository;

import com.rsupport.notice.management.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeCustomRepository {

  @Modifying
  @Query("UPDATE Notice n SET n.viewCount = n.viewCount + :count WHERE n.noticeId = :noticeId")
  void increaseViewCount(@Param("noticeId") Long noticeId, @Param("count") int count);
}
