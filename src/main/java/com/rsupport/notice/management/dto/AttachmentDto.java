package com.rsupport.notice.management.dto;

import com.rsupport.notice.management.entity.Attachment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttachmentDto {
  private Long attachmentId;
  private String fileName;

  public AttachmentDto(Attachment attachment) {
    this.attachmentId = attachment.getAttachmentId();
    this.fileName = attachment.getFileName();
  }
}
