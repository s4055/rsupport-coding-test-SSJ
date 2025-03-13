package com.rsupport.notice.management.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@UtilityClass
public class NoticeUtil {

  public String uploadFile(String uploadDir, MultipartFile multipartFile) {
    try {
      File uploadFolder = new File(uploadDir);

      if (!uploadFolder.exists()) {
        uploadFolder.mkdirs();
      }

      String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
      Path filePath = Paths.get(uploadFolder.getAbsolutePath(), fileName);
      multipartFile.transferTo(filePath.toFile());
      return fileName;
    } catch (IOException e) {
      throw new RuntimeException("파일 저장 실패", e);
    }
  }

  public boolean deleteFile(String uploadDir, String fileName) {
    File file = new File(uploadDir, fileName);

    if (file.exists()) {
      return file.delete();
    } else {
      log.info("파일이 존재하지 않습니다: {}", file.getAbsolutePath());
      return false;
    }
  }
}
