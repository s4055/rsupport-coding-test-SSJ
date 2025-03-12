package com.rsupport.notice.management.service;

import com.rsupport.notice.management.entity.Notice;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

  void addAttachment(Notice notice, MultipartFile multipartFileList);
}
