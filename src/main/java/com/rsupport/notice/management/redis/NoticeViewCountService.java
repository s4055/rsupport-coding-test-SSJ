package com.rsupport.notice.management.redis;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoticeViewCountService {

  private final String VIEW_COUNT_KEY = "notice:viewCount";
  private final RedisTemplate<String, String> redisTemplate;

  /**
   * 조회수 증가
   *
   * @param noticeId the notice id
   */
  public void incrementViewCount(Long noticeId) {
    redisTemplate.opsForHash().increment(VIEW_COUNT_KEY, noticeId.toString(), 1);
  }

  /**
   * 저장된 조회수 모두 조회
   *
   * @return the all view counts
   */
  public Map<Object, Object> getAllViewCounts() {
    return redisTemplate.opsForHash().entries(VIEW_COUNT_KEY);
  }

  /**
   * noticeId 조회수 조회
   *
   * @param noticeId the notice id
   * @return the view count
   */
  public int getViewCount(Long noticeId) {
    Object count = redisTemplate.opsForHash().get(VIEW_COUNT_KEY, noticeId.toString());
    return count != null ? Integer.parseInt(count.toString()) : 0;
  }

  /** 캐시 삭제 */
  public void clearCache() {
    redisTemplate.delete(VIEW_COUNT_KEY);
  }
}
