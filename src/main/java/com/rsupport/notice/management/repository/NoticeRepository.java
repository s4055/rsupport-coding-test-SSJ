package com.rsupport.notice.management.repository;

import com.rsupport.notice.management.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
