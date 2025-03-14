package com.rsupport.notice.management.utils;

import com.rsupport.notice.management.exception.CustomException;
import com.rsupport.notice.management.exception.ErrorCode;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@UtilityClass
public class NoticeUtil {

  private String getExtension(String originFileName) throws CustomException {
    if (originFileName == null || !originFileName.contains(".")) {
      throw new CustomException(ErrorCode.FILE_EXTENSION_FAIL);
    }
    return originFileName.substring(originFileName.lastIndexOf(".") + 1).toLowerCase();
  }

  public String uploadFile(String uploadDir, MultipartFile multipartFile) {
    try {
      File uploadFolder = new File(uploadDir);

      if (!uploadFolder.exists()) {
        uploadFolder.mkdirs();
      }

      String extension = getExtension(multipartFile.getOriginalFilename());
      String fileName = UUID.randomUUID() + "." + extension;
      Path filePath = Paths.get(uploadFolder.getAbsolutePath(), fileName);
      multipartFile.transferTo(filePath.toFile());
      return fileName;
    } catch (Exception e) {
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

  public static String generateDownloadUrl(Long noticeId, Long attachmentId) {
    return ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/notices/{noticeId}/attachments/{attachmentId}/download")
        .buildAndExpand(noticeId, attachmentId)
        .toUriString();
  }
}
