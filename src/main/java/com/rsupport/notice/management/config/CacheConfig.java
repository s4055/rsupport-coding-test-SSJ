package com.rsupport.notice.management.config;

import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {

  @Primary
  @Bean(name = "defaultCacheManager")
  public CacheManager defaultCacheManager(RedisConnectionFactory connectionFactory) {
    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
        .cacheDefaults(getDefaultCacheConfig())
        .build();
  }

  @Bean(name = "getNoticeCacheManager")
  public CacheManager getNoticeCacheManager(RedisConnectionFactory connectionFactory) {
    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
            .cacheDefaults(getDefaultCacheConfig())
            .build();
  }

  @Bean(name = "getNoticesCacheManager")
  public CacheManager getNoticesCacheManager(RedisConnectionFactory connectionFactory) {
    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
        .cacheDefaults(getCacheConfig(Duration.ofMinutes(5L)))
        .build();
  }

  @Bean(name = "searchNoticesCacheManager")
  public CacheManager searchNoticesCacheManager(RedisConnectionFactory connectionFactory) {
    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
        .cacheDefaults(getCacheConfig(Duration.ofMinutes(5L)))
        .build();
  }

  private RedisCacheConfiguration getDefaultCacheConfig() {
    return RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer()));
  }

  private RedisCacheConfiguration getCacheConfig(Duration duration) {
    return getDefaultCacheConfig().entryTtl(duration);
  }
}
