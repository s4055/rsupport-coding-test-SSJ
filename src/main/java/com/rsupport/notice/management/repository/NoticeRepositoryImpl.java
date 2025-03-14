package com.rsupport.notice.management.repository;

import static com.rsupport.notice.management.entity.QNotice.notice;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rsupport.notice.management.entity.Notice;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Notice> searchNotices(
      String keyword, int searchType, LocalDate startDate, LocalDate endDate, Pageable pageable) {

    List<Notice> result =
        queryFactory
            .selectFrom(notice)
            .where(searchKeyword(searchType, keyword), compareDate(startDate, endDate))
            .orderBy(notice.createDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    long total =
        Optional.ofNullable(
                queryFactory
                    .select(notice.count())
                    .from(notice)
                    .where(searchKeyword(searchType, keyword), compareDate(startDate, endDate))
                    .fetchOne())
            .orElse(0L);

    return new PageImpl<>(result, pageable, total);
  }

  private BooleanExpression searchKeyword(int searchType, String keyword) {
    switch (searchType) {
      case 1: // 제목 + 내용
        return notice
            .title
            .containsIgnoreCase(keyword)
            .or(notice.content.containsIgnoreCase(keyword));
      case 2: // 제목
        return notice.title.containsIgnoreCase(keyword);
      default:
        return null;
    }
  }

  private BooleanExpression compareDate(LocalDate startDate, LocalDate endDate) {
    return notice.createDate.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
  }
}
