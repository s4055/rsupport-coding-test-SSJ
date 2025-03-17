package com.rsupport.notice.management;

import java.util.*;
import com.rsupport.notice.management.entity.Notice;
import com.rsupport.notice.management.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadListener {

    private final FileStorageService fileStorageService;

    @Async
    @EventListener
    public void handleFileUpload(FileUploadEvent event) {
        log.info("이벤트 시작");
        List<MultipartFile> files = event.getFiles();
        Notice notice = event.getNotice();
        files.forEach(file -> fileStorageService.storeFileAsync(file, notice));
        log.info("이벤트 종료");
    }
}
