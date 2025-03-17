package com.rsupport.notice.management.controller;

import com.rsupport.notice.management.dto.request.NoticeCreateRequest;
import com.rsupport.notice.management.dto.request.NoticePageRequest;
import com.rsupport.notice.management.dto.request.NoticeSearchRequest;
import com.rsupport.notice.management.dto.request.NoticeUpdateRequest;
import com.rsupport.notice.management.dto.response.*;
import com.rsupport.notice.management.exception.CustomException;
import com.rsupport.notice.management.service.FileStorageService;
import com.rsupport.notice.management.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;
  private final FileStorageService fileStorageService;

  /**
   * 공지사항 등록
   *
   * @param request the request
   * @param multipartFileList the multipart file list
   * @return the response entity
   */
  @Operation(summary = "공지시항 등록")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<NoticeCreateResponse> createNotice(
      @RequestPart(value = "request") @Valid NoticeCreateRequest request,
      @RequestPart(value = "file", required = false) List<MultipartFile> multipartFileList) throws IOException, CustomException {
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
  @Operation(summary = "공지시항 수정")
  @PutMapping(value = "/{noticeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
  @Operation(summary = "공지시항 삭제")
  @Parameters({@Parameter(name = "noticeId", description = "공지사항 식별자", example = "13")})
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
  @Operation(summary = "공지시항 목록")
  @Parameters({
    @Parameter(name = "size", description = "페이지 단위", example = "3"),
    @Parameter(name = "page", description = "조회할 페이지", example = "0")
  })
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
  @Operation(summary = "공지시항 상세")
  @Parameters({@Parameter(name = "noticeId", description = "공지사항 식별자", example = "1")})
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
  @Operation(summary = "공지시항 검색")
  @Parameters({
    @Parameter(name = "searchType", description = "1(제목 + 내용), 2(제목)", example = "1"),
    @Parameter(name = "keyword", description = "검색어", example = "공지"),
    @Parameter(name = "startDate", description = "시작일자", example = "2025-03-01"),
    @Parameter(name = "endDate", description = "종료일자", example = "2025-05-01"),
    @Parameter(name = "size", description = "페이지 단위", example = "3"),
    @Parameter(name = "page", description = "조회할 페이지", example = "0")
  })
  @GetMapping("/search")
  public ResponseEntity<NoticeSearchResponse> searchNotices(
      @ModelAttribute @Valid NoticeSearchRequest request) {
    NoticeSearchResponse response = noticeService.searchNotices(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
