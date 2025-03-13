package com.rsupport.notice.management.repository;

import com.rsupport.notice.management.entity.Notice;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
  Page<Notice> searchNotices(
      String keyword, int searchType, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
