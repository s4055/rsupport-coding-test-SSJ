package com.rsupport.notice.management.dto.common;

import com.rsupport.notice.management.entity.Attachment;
import com.rsupport.notice.management.entity.Notice;
import com.rsupport.notice.management.utils.NoticeUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttachmentDto {
  private Long attachmentId;
  private String fileName;
  private String downloadUrl;

  public AttachmentDto(Attachment attachment, Notice notice) {
    this.attachmentId = attachment.getAttachmentId();
    this.fileName = attachment.getFileName();
    this.downloadUrl = NoticeUtil.generateDownloadUrl(notice.getNoticeId(), this.attachmentId);
  }
}
