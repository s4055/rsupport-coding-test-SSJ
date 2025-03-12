package com.rsupport.notice.management.controller;

import com.rsupport.notice.management.dto.NoticeCreateRequest;
import com.rsupport.notice.management.dto.NoticeCreateResponse;
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
}
