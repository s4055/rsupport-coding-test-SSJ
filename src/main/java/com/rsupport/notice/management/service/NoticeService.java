package com.rsupport.notice.management.service;

import com.rsupport.notice.management.dto.*;

import java.util.List;

import com.rsupport.notice.management.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

public interface NoticeService {

  NoticeCreateResponse createNotice(
      NoticeCreateRequest request, List<MultipartFile> multipartFileList);

  NoticeUpdateResponse updateNotice(
      Long noticeId, NoticeUpdateRequest request, List<MultipartFile> multipartFileList) throws CustomException;

  NoticeDeleteResponse deleteNotice(Long noticeId) throws CustomException;

}
