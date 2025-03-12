package com.rsupport.notice.management.repository;

import com.rsupport.notice.management.entity.Notice;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
  @EntityGraph(attributePaths = {"attachments"})
  @Query(
      "SELECT n FROM Notice n "
          + "WHERE (n.createDate >= :startDate AND n.createDate <= :endDate) "
          + "AND ("
          + "(:searchType = 1 AND (n.title LIKE CONCAT('%', :keyword, '%') OR n.content LIKE CONCAT('%', :keyword, '%'))) "
          + "OR "
          + "(:searchType = 2 AND n.title LIKE CONCAT('%', :keyword, '%'))"
          + ")")
  Page<Notice> findByNoticeSearch(
      @Param("searchType") int searchType,
      @Param("keyword") String keyword,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      Pageable pageable);
}
