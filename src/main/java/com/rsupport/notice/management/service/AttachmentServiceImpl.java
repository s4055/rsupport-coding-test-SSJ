package com.rsupport.notice.management.service;

import com.rsupport.notice.management.dto.request.AttachmentDownloadRequest;
import com.rsupport.notice.management.entity.Attachment;
import com.rsupport.notice.management.exception.CustomException;
import com.rsupport.notice.management.exception.ErrorCode;
import com.rsupport.notice.management.repository.AttachmentRepository;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

  private final AttachmentRepository attachmentRepository;

  /**
   * 첨부파일 다운로드
   *
   * @param request the request
   * @return the response entity
   * @throws CustomException the custom exception
   */
  @Override
  public Resource downloadAttachment(AttachmentDownloadRequest request) throws CustomException {
    Attachment attachment =
        attachmentRepository
            .findById(request.getAttachmentId())
            .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_ATTACHMENT));

    File file = new File(attachment.getFilePath());

    if (!file.exists()) {
      throw new CustomException(ErrorCode.ATTACHMENT_NOT_FOUND_IN_SERVER);
    }

    return new FileSystemResource(file);
  }
}
