package com.rsupport.notice.management.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsupport.notice.management.dto.request.NoticeCreateRequest;
import com.rsupport.notice.management.dto.request.NoticePageRequest;
import com.rsupport.notice.management.dto.request.NoticeSearchRequest;
import com.rsupport.notice.management.dto.request.NoticeUpdateRequest;
import com.rsupport.notice.management.exception.ErrorCode;
import com.rsupport.notice.management.repository.NoticeRepository;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class NoticeControllerTest {

  private final String URL = "/api/notices";

  @Autowired private MockMvc mockMvc;

  @Autowired private NoticeRepository noticeRepository;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @Transactional
  void 공지사항_등록_통합_테스트() throws Exception {
    NoticeCreateRequest request = new NoticeCreateRequest();
    request.setAuthor("작성자");
    request.setTitle("공지사항 제목");
    request.setContent("공지사항 내용");
    request.setStartDate(LocalDateTime.now().plusYears(1));
    request.setEndDate(LocalDateTime.now().plusYears(1).plusMonths(3));

    String content = objectMapper.writeValueAsString(request);

    MockMultipartFile mockMultipartFile =
        new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes());
    MockMultipartFile json =
        new MockMultipartFile(
            "request",
            "",
            MediaType.APPLICATION_JSON_VALUE,
            content.getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart(URL)
                .file(json)
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.OK.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.OK.getMessage())));
  }

  @Test
  @Transactional
  void 공지사항_수정_통합_테스트() throws Exception {
    NoticeUpdateRequest request = new NoticeUpdateRequest();
    request.setTitle("공지사항 제목");
    request.setContent("공지사항 내용");
    request.setStartDate(LocalDateTime.now().plusYears(1));
    request.setEndDate(LocalDateTime.now().plusYears(1).plusMonths(3));

    String content = objectMapper.writeValueAsString(request);

    MockMultipartFile mockMultipartFile =
        new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes());
    MockMultipartFile json =
        new MockMultipartFile(
            "request",
            "",
            MediaType.APPLICATION_JSON_VALUE,
            content.getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart(URL + "/" + 20)
                .file(json)
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(
                    method -> {
                      method.setMethod("PUT");
                      return method;
                    }))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.OK.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.OK.getMessage())));
  }

  @Test
  @Transactional
  void 공지사항_삭제_통합_테스트() throws Exception {
    mockMvc
        .perform(delete(URL + "/" + 10))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.OK.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.OK.getMessage())));
  }

  @Test
  void 공지사항_목록_통합_테스트() throws Exception {
    NoticePageRequest request = new NoticePageRequest(3, 0);
    mockMvc
        .perform(
            get(URL)
                .param("size", String.valueOf(request.getSize()))
                .param("page", String.valueOf(request.getPage())))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.OK.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.OK.getMessage())))
        .andExpect(jsonPath("$.endFlag").exists())
        .andExpect(jsonPath("$.notices").isArray());
  }

  @Test
  void 공지사항_상세_통합_테스트() throws Exception {
    mockMvc
        .perform(get(URL + "/" + 2))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.OK.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.OK.getMessage())))
        .andExpect(jsonPath("$.title", is("공지사항 2")))
        .andExpect(jsonPath("$.content", is("두 번째 공지사항입니다. 업데이트를 확인하세요.")))
        .andExpect(jsonPath("$.createDate").exists())
        .andExpect(jsonPath("$.viewCount", is(5)))
        .andExpect(jsonPath("$.author", is("운영팀")))
        .andExpect(jsonPath("$.attachments").isArray());
  }

  @Test
  void 공지사항_검색_통합_테스트() throws Exception {
    NoticeSearchRequest request =
        new NoticeSearchRequest(1, "공지", LocalDate.now().minusMonths(3), LocalDate.now(), 4, 0);

    mockMvc
        .perform(
            get(URL + "/search")
                .param("searchType", String.valueOf(request.getSearchType()))
                .param("keyword", request.getKeyword())
                .param("startDate", String.valueOf(request.getStartDate()))
                .param("endDate", String.valueOf(request.getEndDate()))
                .param("size", String.valueOf(request.getSize()))
                .param("page", String.valueOf(request.getPage())))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.OK.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.OK.getMessage())))
        .andExpect(jsonPath("$.endFlag").exists())
        .andExpect(jsonPath("$.notices").isArray());
  }
}
