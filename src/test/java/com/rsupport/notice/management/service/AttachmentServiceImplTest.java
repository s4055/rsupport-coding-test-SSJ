package com.rsupport.notice.management.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.rsupport.notice.management.dto.request.*;
import com.rsupport.notice.management.dto.response.*;
import com.rsupport.notice.management.exception.CustomException;
import com.rsupport.notice.management.exception.ErrorCode;
import com.rsupport.notice.management.repository.AttachmentRepository;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AttachmentServiceImplTest {

  @InjectMocks private AttachmentServiceImpl attachmentService;

  @Mock private AttachmentRepository attachmentRepository;

  @Test
  void 첨부파일_조회_실패() {
    Long noticeId = 3L;
    Long attachmentId = 1L;

    AttachmentDownloadRequest request = new AttachmentDownloadRequest(noticeId, attachmentId);

    when(attachmentRepository.findById(attachmentId)).thenReturn(Optional.empty());

    CustomException exception =
        assertThrows(CustomException.class, () -> attachmentService.downloadAttachment(request));
    assertEquals(ErrorCode.NO_SUCH_ATTACHMENT.getResultCode(), exception.getErrorCode());
  }
}
