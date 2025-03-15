package com.rsupport.notice.management.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsupport.notice.management.dto.request.*;
import com.rsupport.notice.management.exception.CustomException;
import com.rsupport.notice.management.service.AttachmentService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class AttachmentControllerTest {

  private final String URL = "/api/notices";

  @Autowired private MockMvc mockMvc;

  @Autowired private AttachmentService attachmentService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void 첨부파일_다운로드_통합_테스트() throws Exception {
    AttachmentDownloadRequest request = new AttachmentDownloadRequest(3L, 1L);
    mockMvc
        .perform(
            get(
                URL + "/{noticeId}/attachments/{fileId}/download",
                request.getNoticeId(),
                request.getAttachmentId()))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
  }
}
