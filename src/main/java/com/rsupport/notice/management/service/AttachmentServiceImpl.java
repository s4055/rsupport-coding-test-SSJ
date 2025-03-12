package com.rsupport.notice.management.service;

import com.rsupport.notice.management.entity.Attachment;
import com.rsupport.notice.management.entity.Notice;
import com.rsupport.notice.management.enums.UseStatus;
import com.rsupport.notice.management.repository.AttachmentRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

  @Value("${file.upload-dir}")
  private String uploadDir;

  private final AttachmentRepository attachmentRepository;

  @Override
  public void addAttachment(Notice notice, MultipartFile multipartFileList) {
    try {
      File uploadFolder = new File(uploadDir);

      if (!uploadFolder.exists()) {
        uploadFolder.mkdirs();
      }

      String fileName = UUID.randomUUID() + "_" + multipartFileList.getOriginalFilename();
      Path filePath = Paths.get(uploadFolder.getAbsolutePath(), fileName);
      multipartFileList.transferTo(filePath.toFile());
      attachmentRepository.save(new Attachment(fileName, "/" + uploadDir + fileName, notice));
    } catch (IOException e) {
      throw new RuntimeException("파일 저장 실패", e);
    }
  }

  @Override
  public void updateAttachment(Notice notice, UseStatus useStatus) {
    attachmentRepository.updateNoticeIsUse(notice.getNoticeId(), useStatus);
  }
}
