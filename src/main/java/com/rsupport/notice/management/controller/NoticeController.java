package com.rsupport.notice.management.controller;

import com.rsupport.notice.management.dto.*;
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

  // 공지시항 등록
  @PostMapping
  public ResponseEntity<NoticeCreateResponse> createNotice(
      @RequestPart(value = "request") @Valid NoticeCreateRequest request,
      @RequestPart(value = "file", required = false) List<MultipartFile> multipartFileList) {
    NoticeCreateResponse response = noticeService.createNotice(request, multipartFileList);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  // 공지사항 수정
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

  // 공지사항 삭제
  @DeleteMapping("/{noticeId}")
  public ResponseEntity<NoticeDeleteResponse> deleteNotice(@PathVariable("noticeId") Long noticeId)
      throws CustomException {
    NoticeDeleteResponse response = noticeService.deleteNotice(noticeId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  // 공지사항 목록
  @GetMapping
  public ResponseEntity<NoticePageResponse> getNotices(
      @ModelAttribute @Valid NoticePageRequest request) {
    NoticePageResponse response = noticeService.getNotices(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  // 공지사항 상세
  @GetMapping("/{noticeId}")
  public ResponseEntity<NoticeDetailResponse> getNotice(@PathVariable("noticeId") Long noticeId)
      throws CustomException {
    NoticeDetailResponse response = noticeService.getNotice(noticeId);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  // 공지사항 검색
  @GetMapping("/search")
  public ResponseEntity<NoticeSearchResponse> searchNotices(
      @ModelAttribute @Valid NoticeSearchRequest request) {
    NoticeSearchResponse response = noticeService.searchNotices(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
