//package com.rsupport.notice.management.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import com.rsupport.notice.management.dto.common.AttachmentDto;
//import com.rsupport.notice.management.dto.request.NoticeCreateRequest;
//import com.rsupport.notice.management.dto.request.NoticePageRequest;
//import com.rsupport.notice.management.dto.request.NoticeSearchRequest;
//import com.rsupport.notice.management.dto.request.NoticeUpdateRequest;
//import com.rsupport.notice.management.dto.response.*;
//import com.rsupport.notice.management.entity.Attachment;
//import com.rsupport.notice.management.entity.Notice;
//import com.rsupport.notice.management.exception.CustomException;
//import com.rsupport.notice.management.exception.ErrorCode;
//import com.rsupport.notice.management.redis.NoticeViewCountService;
//import com.rsupport.notice.management.repository.AttachmentRepository;
//import com.rsupport.notice.management.repository.NoticeRepository;
//import com.rsupport.notice.management.utils.NoticeUtil;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.*;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//@Slf4j
//@ExtendWith(MockitoExtension.class)
//class NoticeServiceImplTest {
//
//  @InjectMocks private NoticeServiceImpl noticeService;
//
//  @Mock private NoticeRepository noticeRepository;
//
//  @Mock private AttachmentRepository attachmentRepository;
//
//  @Mock private NoticeViewCountService viewCountService;
//
//  private final String AUTHOR = "작성자";
//  private final String TITLE = "공지사항 제목";
//  private final String CONTENT = "공지사항 내용";
//  private final LocalDate SD = LocalDate.now();
//  private final LocalDate ED = LocalDate.now().plusMonths(3);
//  private final LocalDateTime SDT = LocalDateTime.now().plusYears(1);
//  private final LocalDateTime EDT = LocalDateTime.now().plusYears(1).plusMonths(3);
//  private final LocalDateTime CDT = LocalDateTime.now();
//
//  @Test
//  void 공지사항_등록_첨부파일_미포함() {
//    NoticeCreateRequest request = new NoticeCreateRequest();
//    request.setAuthor(AUTHOR);
//    request.setTitle(TITLE);
//    request.setContent(CONTENT);
//    request.setStartDate(SDT);
//    request.setEndDate(EDT);
//
//    Notice notice =
//        new Notice(31L, TITLE, CONTENT, SDT, EDT, CDT, 0, AUTHOR, false, Collections.emptyList());
//
//    when(noticeRepository.save(any(Notice.class))).thenReturn(notice);
//
//    NoticeCreateResponse response = noticeService.createNotice(request, Collections.emptyList());
//
//    assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
//    verify(noticeRepository, times(1)).save(any(Notice.class));
//    verify(attachmentRepository, never()).saveAll(anyList());
//  }
//
//  @Test
//  void 공지사항_등록_첨부파일_포함() {
//    NoticeCreateRequest request = new NoticeCreateRequest();
//    request.setAuthor(AUTHOR);
//    request.setTitle(TITLE);
//    request.setContent(CONTENT);
//    request.setStartDate(SDT);
//    request.setEndDate(EDT);
//
//    Notice notice =
//        new Notice(31L, TITLE, CONTENT, SDT, EDT, CDT, 0, AUTHOR, false, Collections.emptyList());
//
//    List<MultipartFile> multipartFileList =
//        Arrays.asList(
//            new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes()),
//            new MockMultipartFile(
//                "file", "test.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes()));
//
//    when(noticeRepository.save(any(Notice.class))).thenReturn(notice);
//
//    try (MockedStatic<NoticeUtil> mockedStatic = mockStatic(NoticeUtil.class)) {
//      mockedStatic
//          .when(() -> NoticeUtil.uploadFile(any(String.class), any(MultipartFile.class)))
//          .thenReturn("test.jpg");
//
//      NoticeCreateResponse response = noticeService.createNotice(request, multipartFileList);
//
//      assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
//      verify(noticeRepository, times(1)).save(any(Notice.class));
//      verify(attachmentRepository, times(1)).saveAll(anyList());
//    }
//  }
//
//  @Test
//  void 공지사항_수정_삭제_미포함_첨부파일_포함() throws CustomException {
//    NoticeUpdateRequest request = new NoticeUpdateRequest();
//    request.setTitle(TITLE);
//    request.setContent(CONTENT);
//    request.setStartDate(SDT);
//    request.setEndDate(EDT);
//
//    List<MultipartFile> multipartFileList =
//        Arrays.asList(
//            new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes()),
//            new MockMultipartFile(
//                "file", "test.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes()));
//
//    Long noticeId = 31L;
//    Notice notice =
//        new Notice(
//            noticeId, TITLE, CONTENT, SDT, EDT, CDT, 0, AUTHOR, false, Collections.emptyList());
//
//    when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));
//    when(noticeRepository.save(any(Notice.class))).thenReturn(notice);
//
//    try (MockedStatic<NoticeUtil> mockedStatic = mockStatic(NoticeUtil.class)) {
//      mockedStatic
//          .when(() -> NoticeUtil.uploadFile(any(String.class), any(MultipartFile.class)))
//          .thenReturn("fileName.jpg");
//
//      NoticeUpdateResponse response =
//          noticeService.updateNotice(noticeId, request, multipartFileList);
//
//      assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
//      assertEquals(ErrorCode.OK.getMessage(), response.getMessage());
//      verify(noticeRepository, times(1)).findById(noticeId);
//      verify(noticeRepository, times(1)).save(notice);
//      verify(attachmentRepository, times(0)).deleteAll(anyList());
//      verify(attachmentRepository, times(1)).saveAll(anyList());
//    }
//  }
//
//  @Test
//  void 공지사항_수정_삭제_미포함_첨부파일_미포함() throws CustomException {
//    NoticeUpdateRequest request = new NoticeUpdateRequest();
//    request.setTitle(TITLE);
//    request.setContent(CONTENT);
//    request.setStartDate(SDT);
//    request.setEndDate(EDT);
//
//    List<MultipartFile> multipartFileList = Collections.emptyList();
//
//    Long noticeId = 31L;
//    Notice notice =
//        new Notice(
//            noticeId, TITLE, CONTENT, SDT, EDT, CDT, 0, AUTHOR, false, Collections.emptyList());
//
//    when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));
//    when(noticeRepository.save(any(Notice.class))).thenReturn(notice);
//
//    NoticeUpdateResponse response =
//        noticeService.updateNotice(noticeId, request, multipartFileList);
//
//    assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
//    assertEquals(ErrorCode.OK.getMessage(), response.getMessage());
//    verify(noticeRepository, times(1)).findById(noticeId);
//    verify(noticeRepository, times(1)).save(notice);
//    verify(attachmentRepository, times(0)).deleteAll(anyList());
//    verify(attachmentRepository, times(0)).saveAll(anyList());
//  }
//
//  @Test
//  void 공지사항_수정_삭제_포함_첨부파일_포함() throws CustomException {
//    NoticeUpdateRequest request = new NoticeUpdateRequest();
//    request.setTitle(TITLE);
//    request.setContent(CONTENT);
//    request.setStartDate(SDT);
//    request.setEndDate(EDT);
//    request.setDeleteAttachments(
//        Arrays.asList(
//            new AttachmentDto(1L, "file1", "download1"),
//            new AttachmentDto(2L, "file2", "download2")));
//
//    List<MultipartFile> multipartFileList =
//        Arrays.asList(
//            new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes()),
//            new MockMultipartFile(
//                "file", "test.png", MediaType.IMAGE_PNG_VALUE, "test".getBytes()));
//
//    Long noticeId = 31L;
//    Notice notice =
//        new Notice(
//            noticeId, TITLE, CONTENT, SDT, EDT, CDT, 0, AUTHOR, false, Collections.emptyList());
//
//    when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));
//    when(noticeRepository.save(any(Notice.class))).thenReturn(notice);
//
//    try (MockedStatic<NoticeUtil> mockedStatic = mockStatic(NoticeUtil.class)) {
//      mockedStatic
//          .when(() -> NoticeUtil.uploadFile(any(String.class), any(MultipartFile.class)))
//          .thenReturn("fileName.jpg");
//      mockedStatic.when(() -> NoticeUtil.deleteFile(anyString(), anyString())).thenReturn(true);
//
//      NoticeUpdateResponse response =
//          noticeService.updateNotice(noticeId, request, multipartFileList);
//
//      assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
//      assertEquals(ErrorCode.OK.getMessage(), response.getMessage());
//      verify(noticeRepository, times(1)).findById(noticeId);
//      verify(noticeRepository, times(1)).save(notice);
//      verify(attachmentRepository, times(1)).deleteAll(anyList());
//      verify(attachmentRepository, times(1)).saveAll(anyList());
//    }
//  }
//
//  @Test
//  void 공지사항_수정_삭제_포함_첨부파일_미포함() throws CustomException {
//    NoticeUpdateRequest request = new NoticeUpdateRequest();
//    request.setTitle(TITLE);
//    request.setContent(CONTENT);
//    request.setStartDate(SDT);
//    request.setEndDate(EDT);
//    request.setDeleteAttachments(
//        Arrays.asList(
//            new AttachmentDto(1L, "file1", "download1"),
//            new AttachmentDto(2L, "file2", "download2")));
//
//    Long noticeId = 31L;
//    Notice notice =
//        new Notice(
//            noticeId, TITLE, CONTENT, SDT, EDT, CDT, 0, AUTHOR, false, Collections.emptyList());
//
//    when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));
//    when(noticeRepository.save(any(Notice.class))).thenReturn(notice);
//
//    try (MockedStatic<NoticeUtil> mockedStatic = mockStatic(NoticeUtil.class)) {
//      mockedStatic
//          .when(() -> NoticeUtil.uploadFile(any(String.class), any(MultipartFile.class)))
//          .thenReturn("fileName.jpg");
//      mockedStatic.when(() -> NoticeUtil.deleteFile(anyString(), anyString())).thenReturn(true);
//
//      NoticeUpdateResponse response =
//          noticeService.updateNotice(noticeId, request, Collections.emptyList());
//
//      assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
//      assertEquals(ErrorCode.OK.getMessage(), response.getMessage());
//      verify(noticeRepository, times(1)).findById(noticeId);
//      verify(noticeRepository, times(1)).save(notice);
//      verify(attachmentRepository, times(1)).deleteAll(anyList());
//      verify(attachmentRepository, times(0)).saveAll(anyList());
//    }
//  }
//
//  @Test
//  void 공지사항_수정_예외() {
//    Long noticeId = 100L;
//    NoticeUpdateRequest request = new NoticeUpdateRequest();
//    request.setTitle(TITLE);
//    request.setContent(CONTENT);
//    request.setStartDate(SDT);
//    request.setEndDate(EDT);
//
//    when(noticeRepository.findById(noticeId)).thenReturn(Optional.empty());
//
//    assertThrows(
//        CustomException.class,
//        () -> noticeService.updateNotice(noticeId, request, Collections.emptyList()));
//  }
//
//  @Test
//  void 공지사항_삭제() throws CustomException {
//    Long noticeId = 31L;
//    Notice notice = new Notice(noticeId, TITLE, CONTENT, SDT, EDT, CDT, 0, AUTHOR, true);
//    Attachment attachment1 = new Attachment("origin1", "file1", "path1");
//    Attachment attachment2 = new Attachment("origin2", "file2", "path2");
//    notice.addAttachment(attachment1);
//    notice.addAttachment(attachment2);
//    List<Attachment> attachments = Arrays.asList(attachment1, attachment2);
//
//    try (MockedStatic<NoticeUtil> mockedStatic = mockStatic(NoticeUtil.class)) {
//      when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));
//      when(attachmentRepository.findByNotice_noticeId(noticeId)).thenReturn(attachments);
//      doNothing().when(attachmentRepository).deleteByFiles(noticeId);
//      mockedStatic.when(() -> NoticeUtil.deleteFile(anyString(), anyString())).thenReturn(true);
//
//      NoticeDeleteResponse response = noticeService.deleteNotice(noticeId);
//
//      assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
//      assertEquals(ErrorCode.OK.getMessage(), response.getMessage());
//
//      verify(noticeRepository, times(1)).findById(noticeId);
//      verify(attachmentRepository, times(1)).deleteByFiles(noticeId);
//      verify(noticeRepository, times(1)).delete(notice);
//    }
//  }
//
//  @Test
//  void 공지사항_삭제_예외() {
//    Long noticeId = 999L;
//
//    when(noticeRepository.findById(noticeId)).thenReturn(Optional.empty());
//
//    assertThrows(CustomException.class, () -> noticeService.deleteNotice(noticeId));
//  }
//
//  @Test
//  void 공지사항_목록_조회() {
//    NoticePageRequest request = new NoticePageRequest(3, 0);
//
//    Notice notice1 =
//        new Notice(31L, TITLE, CONTENT, SDT, EDT, CDT, 0, AUTHOR, false, Collections.emptyList());
//    Notice notice2 =
//        new Notice(32L, TITLE, CONTENT, SDT, EDT, CDT, 0, AUTHOR, false, Collections.emptyList());
//
//    Page<Notice> result = new PageImpl<>(Arrays.asList(notice1, notice2));
//    when(noticeRepository.findAll(any(Pageable.class))).thenReturn(result);
//
//    NoticePageResponse response = noticeService.getNotices(request);
//
//    assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
//    assertEquals(2, response.getNotices().size());
//    verify(noticeRepository, times(1)).findAll(any(Pageable.class));
//  }
//
//  @Test
//  void 공지사항_조회() throws CustomException {
//    Long noticeId = 31L;
//    Notice notice =
//        new Notice(
//            noticeId, TITLE, CONTENT, SDT, EDT, CDT, 0, AUTHOR, false, Collections.emptyList());
//    when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));
//    when(viewCountService.getViewCount(noticeId)).thenReturn(10);
//    doNothing().when(viewCountService).incrementViewCount(noticeId);
//
//    NoticeDetailResponse response = noticeService.getNotice(noticeId);
//
//    assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
//    assertEquals(TITLE, response.getTitle());
//    verify(noticeRepository, times(1)).findById(noticeId);
//    verify(viewCountService, times(1)).getViewCount(noticeId);
//    verify(viewCountService, times(1)).incrementViewCount(noticeId);
//  }
//
//  @Test
//  void 공지사항_조회_예외() {
//    Long noticeId = 100L;
//
//    when(noticeRepository.findById(noticeId)).thenReturn(Optional.empty());
//
//    assertThrows(CustomException.class, () -> noticeService.getNotice(noticeId));
//  }
//
//  @Test
//  void 공지사항_검색() {
//    NoticeSearchRequest request = new NoticeSearchRequest(1, "공지", SD, ED, 3, 0);
//    PageRequest pageable = PageRequest.of(request.getPage(), request.getSize());
//
//    Notice notice1 =
//        new Notice(31L, TITLE, CONTENT, SDT, EDT, CDT, 0, AUTHOR, false, Collections.emptyList());
//    Notice notice2 =
//        new Notice(32L, TITLE, CONTENT, SDT, EDT, CDT, 0, AUTHOR, false, Collections.emptyList());
//
//    Page<Notice> noticePage = new PageImpl<>(Arrays.asList(notice1, notice2));
//
//    when(noticeRepository.searchNotices(
//            anyString(),
//            anyInt(),
//            any(LocalDate.class),
//            any(LocalDate.class),
//            any(PageRequest.class)))
//        .thenReturn(noticePage);
//
//    NoticeSearchResponse response = noticeService.searchNotices(request);
//
//    assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
//    assertEquals(2, response.getNotices().size());
//    verify(noticeRepository, times(1)).searchNotices(eq("공지"), eq(1), eq(SD), eq(ED), eq(pageable));
//  }
//}
