package com.rsupport.notice.management.controller;

import com.rsupport.notice.management.dto.request.*;
import com.rsupport.notice.management.dto.response.*;
import com.rsupport.notice.management.exception.CustomException;
import com.rsupport.notice.management.service.AttachmentService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class AttachmentController {

  private final AttachmentService attachmentService;

  /**
   * 첨부파일 다운로드
   *
   * @param request the request
   * @return the response entity
   * @throws CustomException the custom exception
   */
  @GetMapping("/{noticeId}/attachments/{attachmentId}/download")
  public ResponseEntity<Resource> downloadAttachment(
      @ModelAttribute @Valid AttachmentDownloadRequest request) throws CustomException {
    Resource response = attachmentService.downloadAttachment(request);
    return ResponseEntity.status(HttpStatus.OK)
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + response.getFilename() + "\"")
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(response);
  }
}
