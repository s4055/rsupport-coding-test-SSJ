package com.rsupport.notice.management.dto;

import com.rsupport.notice.management.entity.Attachment;
import com.rsupport.notice.management.enums.UseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttachmentDto {
  private Long attachmentId;
  private String fileName;
  private UseStatus isUse;

  public AttachmentDto(Attachment attachment) {
    this.attachmentId = attachment.getAttachmentId();
    this.fileName = attachment.getFileName();
    this.isUse = attachment.getIsUse();
  }
}
