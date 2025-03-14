package com.rsupport.notice.management.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttachmentDownloadRequest {

  @NotNull(message = "공지사항 식별자는 필수 입니다.")
  private Long noticeId;

  @NotNull(message = "첨부파일 식별자는 필수 입니다.")
  private Long attachmentId;
}
