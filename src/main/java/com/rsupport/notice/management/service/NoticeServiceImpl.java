package com.rsupport.notice.management.service;

import com.rsupport.notice.management.dto.common.AttachmentDto;
import com.rsupport.notice.management.dto.request.NoticeCreateRequest;
import com.rsupport.notice.management.dto.request.NoticePageRequest;
import com.rsupport.notice.management.dto.request.NoticeSearchRequest;
import com.rsupport.notice.management.dto.request.NoticeUpdateRequest;
import com.rsupport.notice.management.dto.response.*;
import com.rsupport.notice.management.entity.Attachment;
import com.rsupport.notice.management.entity.Notice;
import com.rsupport.notice.management.exception.CustomException;
import com.rsupport.notice.management.exception.ErrorCode;
import com.rsupport.notice.management.redis.NoticeViewCountService;
import com.rsupport.notice.management.repository.AttachmentRepository;
import com.rsupport.notice.management.repository.NoticeRepository;
import com.rsupport.notice.management.utils.NoticeUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

  @Value("${file.upload-dir}")
  private String uploadDir;

  private final NoticeViewCountService viewCountService;
  private final NoticeRepository noticeRepository;
  private final AttachmentRepository attachmentRepository;
  
  @Transactional
  @Override
  public NoticeCreateResponse createNotice(
          NoticeCreateRequest request, List<MultipartFile> multipartFileList) {
    boolean hasAttachment = multipartFileList != null && !multipartFileList.isEmpty();
    Notice notice = noticeRepository.save(new Notice(request, hasAttachment));
    log.info("공지사항 등록 = {}", notice.getNoticeId());

    if (hasAttachment) {
      List<Attachment> newAttachments = new ArrayList<>();
      for (MultipartFile multipartFile : multipartFileList) {
        String fileName = NoticeUtil.uploadFile(uploadDir, multipartFile);
        newAttachments.add(new Attachment(fileName, "/" + uploadDir + fileName, notice));
      }
      attachmentRepository.saveAll(newAttachments);
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

    boolean hasAttachment = multipartFileList != null && !multipartFileList.isEmpty();
    boolean isExistAttachments =
        request.getAttachments() != null && !request.getAttachments().isEmpty();

    notice.updateNotice(request, hasAttachment, isExistAttachments);

    noticeRepository.save(notice);

    // 기존 첨부파일 조회
    List<Attachment> attachments = attachmentRepository.findByNotice_noticeId(notice.getNoticeId());
    Set<String> oldFileNames =
        request.getAttachments().stream()
            .map(AttachmentDto::getFileName)
            .collect(Collectors.toSet());

    // 변경된 첨부파일 목록 (삭제된 첨부파일 찾기)
    List<Attachment> filesToDelete =
        attachments.stream()
            .filter(file -> !oldFileNames.contains(file.getFileName()))
            .collect(Collectors.toList());

    // 첨부파일 삭제
    if (!filesToDelete.isEmpty()) {
      filesToDelete.forEach(
          file -> {
            NoticeUtil.deleteFile(uploadDir, file.getFileName());
            attachmentRepository.delete(file);
          });
    }

    // 신규 첨부파일 저장
    if (hasAttachment) {
      List<Attachment> newAttachments = new ArrayList<>();
      for (MultipartFile multipartFile : multipartFileList) {
        String fileName = NoticeUtil.uploadFile(uploadDir, multipartFile);
        newAttachments.add(new Attachment(fileName, "/" + uploadDir + fileName, notice));
      }
      attachmentRepository.saveAll(newAttachments);
    }

    return new NoticeUpdateResponse(ErrorCode.OK.getResultCode(), ErrorCode.OK.getMessage());
  }

  @Transactional
  @Override
  public NoticeDeleteResponse deleteNotice(Long noticeId) throws CustomException {
    Notice notice =
        noticeRepository
            .findById(noticeId)
            .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_NOTICE));

    List<Attachment> attachments = attachmentRepository.findByNotice_noticeId(notice.getNoticeId());

    List<String> fileNames =
        attachments.stream().map(Attachment::getFileName).collect(Collectors.toList());

    for (String fileName : fileNames) {
      NoticeUtil.deleteFile(uploadDir, fileName);
    }

    attachmentRepository.deleteByFiles(notice.getNoticeId());

    noticeRepository.delete(notice);

    return new NoticeDeleteResponse(ErrorCode.OK.getResultCode(), ErrorCode.OK.getMessage());
  }

  @Override
  public NoticePageResponse getNotices(NoticePageRequest request) {
    PageRequest pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Notice> notices = noticeRepository.findAll(pageable);
    return new NoticePageResponse(ErrorCode.OK.getResultCode(), ErrorCode.OK.getMessage(), notices);
  }

  @Override
  public NoticeDetailResponse getNotice(Long noticeId) throws CustomException {
    Notice notice =
        noticeRepository
            .findById(noticeId)
            .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_NOTICE));
    int redisViewCount = viewCountService.getViewCount(noticeId);
    viewCountService.incrementViewCount(noticeId);
    return new NoticeDetailResponse(
        ErrorCode.OK.getResultCode(), ErrorCode.OK.getMessage(), notice, redisViewCount);
  }

  @Override
  public NoticeSearchResponse searchNotices(NoticeSearchRequest request) {
    PageRequest pageable = PageRequest.of(request.getPage(), request.getSize());

    Page<Notice> notices =
        noticeRepository.searchNotices(
            request.getKeyword(),
            request.getSearchType(),
            request.getStartDate(),
            request.getEndDate(),
            pageable);

    return new NoticeSearchResponse(
        ErrorCode.OK.getResultCode(), ErrorCode.OK.getMessage(), notices);
  }
}
