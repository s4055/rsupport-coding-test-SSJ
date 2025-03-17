package com.rsupport.notice.management.service;

import com.rsupport.notice.management.entity.Attachment;
import com.rsupport.notice.management.entity.Notice;
import com.rsupport.notice.management.repository.AttachmentRepository;
import com.rsupport.notice.management.utils.NoticeUtil;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {

  private final AttachmentRepository attachmentRepository;

  @Async(value = "asyncTaskExecutor")
  public void uploadFiles(List<MultipartFile> files, Notice notice) {
    log.info("비동기 시작");
    for (MultipartFile file : files) {
      log.info("=> {}", file.getOriginalFilename());
    }
    log.info("비동기 종료");
    //    return CompletableFuture.completedFuture("파일 업로드 완료");
  }

  @Async(value = "asyncTaskExecutor")
  public void upload(MultipartFile files, Notice notice) {
    log.info("비동기 시작 하나");
    log.info("=> {}", files.getOriginalFilename());
    log.info("비동기 종료 하나");
    //    return CompletableFuture.completedFuture(null);
  }

  @Async("asyncTaskExecutor")
  public CompletableFuture<List<String>> processAsyncTask() throws InterruptedException {
    //    Thread.sleep(100000);
    //    return CompletableFuture.completedFuture("Task Completed");
    List<CompletableFuture<String>> futures = new ArrayList<>();

    for (int i = 0; i < 1000; i++) {
      final int taskId = i;
      // 각 작업을 비동기적으로 실행하고, 결과를 CompletableFuture로 처리
      CompletableFuture<String> future =
          CompletableFuture.supplyAsync(
              () -> {
                try {
                  log.info("=> {}", taskId);
                  Thread.sleep(1000); // 예: 1초 대기
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
                return "Task " + taskId + " completed";
              });
      futures.add(future); // 결과를 futures 리스트에 추가
    }

    // 모든 작업이 완료된 후 결과를 반환
    List<String> results =
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(
                v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()))
            .join();

    return CompletableFuture.completedFuture(results);
  }

  @Async("asyncTaskExecutor")
  public CompletableFuture<List<String>> test(List<MultipartFile> fileList) {
    List<CompletableFuture<String>> futures = new ArrayList<>();

    int i = 0;
    for (MultipartFile file : fileList) {
      final int taskId = i;
      CompletableFuture<String> future =
          CompletableFuture.supplyAsync(
              () -> {
                log.info("=============== 시작 ===============");
                log.info("=> {}", file.toString());
                log.info("=============== 종료 ===============");
                return "Task " + taskId + " completed";
              });
      futures.add(future); // 결과를 futures 리스트에 추가
      i++;
    }

    // 모든 작업이 완료된 후 결과를 반환
    List<String> results =
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(
                v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()))
            .join();

    return CompletableFuture.completedFuture(results);
  }

  @Async("asyncTaskExecutor")
  public CompletableFuture<Attachment> storeFileAsync(MultipartFile file, Notice notice) {
    log.info("비동기 시작");
    try {
      File uploadFolder = new File("upload/");
      String extension = NoticeUtil.getExtension(file.getOriginalFilename());
      String fileName = UUID.randomUUID() + "." + extension;
      Path filePath = Paths.get(uploadFolder.getAbsolutePath(), fileName);
      file.transferTo(filePath.toFile());
      Attachment attachment =
          attachmentRepository.save(
              new Attachment(file.getOriginalFilename(), fileName, "upload", notice));
      log.info("비동기 종료");
      return CompletableFuture.completedFuture(attachment);
    } catch (Exception e) {
      throw new RuntimeException("dddd");
    }
  }

  @Async("asyncTaskExecutor")
  public void storeFilesAsync(List<MultipartFile> files) {
    log.info("11111111111111");
    for (MultipartFile file : files) {
      try {
        File uploadFolder = new File("upload/");
        String extension = NoticeUtil.getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + extension;
        Path filePath = Paths.get(uploadFolder.getAbsolutePath(), fileName);
        file.transferTo(filePath.toFile());
        log.info("bbbbbbbbbbbbbbbb");
      } catch (Exception e) {
        log.info("{}", e);
        throw new RuntimeException("bbbb");
      }
    }
    log.info("22222222222222222222");
  }
}
