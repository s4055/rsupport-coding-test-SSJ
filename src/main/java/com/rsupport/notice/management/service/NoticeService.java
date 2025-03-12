package com.rsupport.notice.management.service;

import com.rsupport.notice.management.dto.NoticeCreateRequest;
import com.rsupport.notice.management.dto.NoticeCreateResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface NoticeService {

  NoticeCreateResponse createNotice(
      NoticeCreateRequest request, List<MultipartFile> multipartFileList);
}
