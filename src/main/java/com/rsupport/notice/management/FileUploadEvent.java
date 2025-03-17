package com.rsupport.notice.management;

import com.rsupport.notice.management.entity.Notice;
import java.util.List;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class FileUploadEvent {
  private final Notice notice;
  private final List<MultipartFile> files;

  public FileUploadEvent(Notice notice, List<MultipartFile> files) {
    this.notice = notice;
    this.files = files;
  }
}
