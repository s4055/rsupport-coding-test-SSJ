package com.rsupport.notice.management.service;

import com.rsupport.notice.management.dto.NoticeCreateRequest;
import com.rsupport.notice.management.dto.NoticeCreateResponse;
import com.rsupport.notice.management.dto.NoticeUpdateRequest;
import com.rsupport.notice.management.dto.NoticeUpdateResponse;
import com.rsupport.notice.management.entity.Notice;
import com.rsupport.notice.management.enums.UseStatus;
import com.rsupport.notice.management.exception.CustomException;
import com.rsupport.notice.management.exception.ErrorCode;
import com.rsupport.notice.management.repository.NoticeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

  private final NoticeRepository noticeRepository;
  private final AttachmentService attachmentService;

  @Transactional
  @Override
  public NoticeCreateResponse createNotice(
      NoticeCreateRequest request, List<MultipartFile> multipartFileList) {
    Notice notice = noticeRepository.save(new Notice(request));
    log.info("공지사항 등록 = {}", notice.getNoticeId());

    if (multipartFileList != null && !multipartFileList.isEmpty()) {
      multipartFileList.forEach(file -> attachmentService.addAttachment(notice, file));
    }

    return new NoticeCreateResponse(ErrorCode.OK.getResultCode(), ErrorCode.OK.getMessage());
  }

  @Transactional
  @Override
  public NoticeUpdateResponse updateNotice(
      Long noticeId, NoticeUpdateRequest request, List<MultipartFile> multipartFileList)
      throws CustomException {
    Notice notice =
        noticeRepository
            .findById(noticeId)
            .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_NOTICE));
    notice.updateNotice(request);
    noticeRepository.save(notice);

    attachmentService.updateAttachment(notice, UseStatus.N);

    if (multipartFileList != null && !multipartFileList.isEmpty()) {
      multipartFileList.forEach(file -> attachmentService.addAttachment(notice, file));
    }

    return new NoticeUpdateResponse(ErrorCode.OK.getResultCode(), ErrorCode.OK.getMessage());
  }

}
