package com.rsupport.notice.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "asyncTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(200);   // 기본 실행할 스레드 개수
        executor.setMaxPoolSize(300);   // 최대 스레드 개수
        executor.setQueueCapacity(400); // 큐에 저장할 요청 개수
        executor.setThreadNamePrefix("Async-");
//        executor.setKeepAliveSeconds(60);       // 유휴 상태의 스레드를 60초 후 종료
        executor.initialize();
        return executor;
    }
}