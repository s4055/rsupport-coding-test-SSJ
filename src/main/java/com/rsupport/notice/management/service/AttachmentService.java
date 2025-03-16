package com.rsupport.notice.management.service;

import com.rsupport.notice.management.dto.request.AttachmentDownloadRequest;
import com.rsupport.notice.management.exception.CustomException;
import org.springframework.core.io.Resource;

public interface AttachmentService {
  /**
   * 첨부파일 다운로드
   *
   * @param request the request
   * @return the resource
   * @throws CustomException the custom exception
   */
  Resource downloadAttachment(AttachmentDownloadRequest request) throws CustomException;
}
