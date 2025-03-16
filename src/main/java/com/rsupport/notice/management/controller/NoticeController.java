package com.rsupport.notice.management.controller;

import com.rsupport.notice.management.dto.request.NoticeCreateRequest;
import com.rsupport.notice.management.dto.request.NoticePageRequest;
import com.rsupport.notice.management.dto.request.NoticeSearchRequest;
import com.rsupport.notice.management.dto.request.NoticeUpdateRequest;
import com.rsupport.notice.management.dto.response.*;
import com.rsupport.notice.management.exception.CustomException;
import com.rsupport.notice.management.service.NoticeService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;

  /**
   * 공지사항 등록
   *
   * @param request the request
   * @param multipartFileList the multipart file list
   * @return the response entity
   */
  @PostMapping
  public ResponseEntity<NoticeCreateResponse> createNotice(
      @RequestPart(value = "request") @Valid NoticeCreateRequest request,
      @RequestPart(value = "file", required = false) List<MultipartFile> multipartFileList) {
    NoticeCreateResponse response = noticeService.createNotice(request, multipartFileList);
    return ResponseEntity.status(HttpStatus.OK).body(response);
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
  @PutMapping("/{noticeId}")
  public ResponseEntity<NoticeUpdateResponse> updateNotice(
      @PathVariable("noticeId") Long noticeId,
      @RequestPart(value = "request") @Valid NoticeUpdateRequest request,
      @RequestPart(value = "file", required = false) List<MultipartFile> multipartFileList)
      throws CustomException {
    NoticeUpdateResponse response =
        noticeService.updateNotice(noticeId, request, multipartFileList);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 공지시항 삭제
   *
   * @param noticeId the notice id
   * @return the response entity
   * @throws CustomException the custom exception
   */
  @DeleteMapping("/{noticeId}")
  public ResponseEntity<NoticeDeleteResponse> deleteNotice(@PathVariable("noticeId") Long noticeId)
      throws CustomException {
    NoticeDeleteResponse response = noticeService.deleteNotice(noticeId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 공지사항 목록
   *
   * @param request the request
   * @return the notices
   */
  @GetMapping
  public ResponseEntity<NoticePageResponse> getNotices(
      @ModelAttribute @Valid NoticePageRequest request) {
    NoticePageResponse response = noticeService.getNotices(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 공지시항 상세
   *
   * @param noticeId the notice id
   * @return the notice
   * @throws CustomException the custom exception
   */
  @GetMapping("/{noticeId}")
  public ResponseEntity<NoticeDetailResponse> getNotice(@PathVariable("noticeId") Long noticeId)
      throws CustomException {
    NoticeDetailResponse response = noticeService.getNotice(noticeId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 공지사항 검색
   *
   * @param request the request
   * @return the response entity
   */
  @GetMapping("/search")
  public ResponseEntity<NoticeSearchResponse> searchNotices(
      @ModelAttribute @Valid NoticeSearchRequest request) {
    NoticeSearchResponse response = noticeService.searchNotices(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
