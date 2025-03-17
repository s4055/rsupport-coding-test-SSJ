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

import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
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
  private final FileStorageService fileStorageService;

  /**
   * 공지사항 등록
   *
   * @param request the request
   * @param multipartFileList the multipart file list
   * @return the response entity
   */
  @Async(value = "asyncTaskExecutor")
  @Override
  @Transactional
  @CacheEvict(value = "notices", allEntries = true)
  public NoticeCreateResponse createNotice(
      NoticeCreateRequest request, List<MultipartFile> multipartFileList) {
    boolean hasAttachment = multipartFileList != null && !multipartFileList.isEmpty();
    Notice notice = noticeRepository.save(new Notice(request, hasAttachment));
    log.info("공지사항 등록 = {}", notice.getNoticeId());

    List<Map<String, String>> fileList = new ArrayList<>();
    List<Attachment> newAttachments = new ArrayList<>();
    if (hasAttachment) {
      for (MultipartFile multipartFile : multipartFileList) {
        Map<String, String> map = new HashMap<>();
        String fileName = UUID.randomUUID().toString();
        newAttachments.add(new Attachment(multipartFile.getOriginalFilename(), fileName, uploadDir, notice));
      }
      attachmentRepository.saveAll(newAttachments);
      fileStorageService.test(multipartFileList);
      //      List<Attachment> newAttachments = new ArrayList<>();
      //      for (MultipartFile multipartFile : multipartFileList) {
      //        String fileName = NoticeUtil.uploadFile(uploadDir, multipartFile);
      //        newAttachments.add(
      //            new Attachment(multipartFile.getOriginalFilename(), fileName, uploadDir,
      // notice));
      //      }
      //      attachmentRepository.saveAll(newAttachments);
    }

    return new NoticeCreateResponse(ErrorCode.OK.getResultCode(), ErrorCode.OK.getMessage());
  }

  /**
   * 공지사항 수정
   *
   * @param noticeId the notice id
   * @param request the request
   * @param multipartFileList the multipart file list
   * @return the response entity
   * @throws CustomException the custom exception
   */
  @Override
  @Transactional
  @CacheEvict(value = "notices", key = "#noticeId")
  public NoticeUpdateResponse updateNotice(
      Long noticeId, NoticeUpdateRequest request, List<MultipartFile> multipartFileList)
      throws CustomException {
    Notice notice =
        noticeRepository
            .findById(noticeId)
            .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_NOTICE));

    boolean hasAttachment = multipartFileList != null && !multipartFileList.isEmpty();

    if (request.getDeleteAttachments() != null && !request.getDeleteAttachments().isEmpty()) {
      List<Long> deleteId =
          request.getDeleteAttachments().stream()
              .map(AttachmentDto::getAttachmentId)
              .collect(Collectors.toList());
      List<Attachment> delAttachments = attachmentRepository.findAllById(deleteId);
      delAttachments.forEach(file -> NoticeUtil.deleteFile(uploadDir, file.getFileName()));
      attachmentRepository.deleteAll(delAttachments);

      hasAttachment = delAttachments.size() > deleteId.size();
    }

    if (multipartFileList != null && !multipartFileList.isEmpty()) {
      List<Attachment> newAttachments = new ArrayList<>();
      for (MultipartFile multipartFile : multipartFileList) {
        String fileName = NoticeUtil.uploadFile(uploadDir, multipartFile);
        newAttachments.add(
            new Attachment(multipartFile.getOriginalFilename(), fileName, uploadDir, notice));
      }
      attachmentRepository.saveAll(newAttachments);
    }

    notice.updateNotice(request, hasAttachment);

    noticeRepository.save(notice);

    return new NoticeUpdateResponse(ErrorCode.OK.getResultCode(), ErrorCode.OK.getMessage());
  }

  /**
   * 공지시항 삭제
   *
   * @param noticeId the notice id
   * @return the response entity
   * @throws CustomException the custom exception
   */
  @Override
  @Transactional
  @CacheEvict(value = "notices", key = "#noticeId")
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

  /**
   * 공지사항 목록
   *
   * @param request the request
   * @return the notices
   */
  @Override
  @Cacheable(
      cacheManager = "getNoticesCacheManager",
      value = "notices",
      key = "#request.page + '_' + #request.size")
  public NoticePageResponse getNotices(NoticePageRequest request) {
    PageRequest pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Notice> notices = noticeRepository.findAll(pageable);
    return new NoticePageResponse(ErrorCode.OK.getResultCode(), ErrorCode.OK.getMessage(), notices);
  }

  /**
   * 공지시항 상세
   *
   * @param noticeId the notice id
   * @return the notice
   * @throws CustomException the custom exception
   */
  @Override
  @CachePut(cacheManager = "getNoticeCacheManager", value = "notices", key = "#noticeId")
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

  /**
   * 공지사항 검색
   *
   * @param request the request
   * @return the response entity
   */
  @Override
  @Cacheable(
      cacheManager = "searchNoticesCacheManager",
      value = "notices",
      key =
          "#request.searchType + '_' + #request.keyword + '_' + #request.formattedStartDate + '_' + #request.getFormattedEndDate + '_' + #request.page + '_' + #request.size")
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
