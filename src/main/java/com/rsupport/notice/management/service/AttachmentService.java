package com.rsupport.notice.management.service;

import com.rsupport.notice.management.entity.Notice;
import com.rsupport.notice.management.enums.UseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {

  void addAttachment(Notice notice, MultipartFile multipartFileList);

  void updateAttachment(Notice notice, UseStatus useStatus);
}
