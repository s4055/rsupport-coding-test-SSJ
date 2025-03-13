package com.rsupport.notice.management.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rsupport.notice.management.entity.Notice;
import com.rsupport.notice.management.entity.QNotice;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeImplRepository implements NoticeCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Notice> searchNotices(
      String keyword, int searchType, LocalDate startDate, LocalDate endDate, Pageable pageable) {
    QNotice notice = QNotice.notice;

    BooleanBuilder builder = new BooleanBuilder();

    // 제목 + 내용
    if (searchType == 1) {
      builder.and(
          notice.title.containsIgnoreCase(keyword).or(notice.content.containsIgnoreCase(keyword)));
    }

    // 제목
    if (searchType == 2) {
      builder.and(notice.title.containsIgnoreCase(keyword));
    }

    // 등록일자 범위 조건
    builder.and(notice.createDate.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)));

    // 쿼리 실행
    List<Notice> result =
        queryFactory
            .selectFrom(notice)
            .where(builder)
            .orderBy(notice.createDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    // 전체 개수 조회
    long total = queryFactory.select(notice.count()).from(notice).where(builder).fetchOne();

    return new PageImpl<>(result, pageable, total);
  }
}
