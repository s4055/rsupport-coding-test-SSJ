package com.rsupport.notice.management.scheduler;

import com.rsupport.notice.management.redis.NoticeViewCountService;
import com.rsupport.notice.management.repository.NoticeRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoticeViewCountBatchService {

  private final NoticeRepository noticeRepository;
  private final NoticeViewCountService viewCountService;

  @Transactional
  @Scheduled(fixedDelay = 60000) // 1분마다 실행
  public void syncViewCountsToDatabase() {
    log.info("=============== syncViewCountsToDatabase ===============");
    Map<Object, Object> viewCounts = viewCountService.getAllViewCounts();

    for (Map.Entry<Object, Object> entry : viewCounts.entrySet()) {
      Long noticeId = Long.parseLong(entry.getKey().toString());
      int count = Integer.parseInt(entry.getValue().toString());

      noticeRepository.increaseViewCount(noticeId, count); // DB 업데이트
    }
    viewCountService.clearCache(); // Redis 초기화
  }
}
