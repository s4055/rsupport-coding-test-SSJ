package com.rsupport.notice.management.service;

import com.rsupport.notice.management.dto.request.AttachmentDownloadRequest;
import com.rsupport.notice.management.exception.CustomException;
import org.springframework.core.io.Resource;

public interface AttachmentService {
  Resource downloadAttachment(AttachmentDownloadRequest request) throws CustomException;
}
