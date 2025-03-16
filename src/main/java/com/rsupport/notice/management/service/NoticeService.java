package com.rsupport.notice.management.service;

import com.rsupport.notice.management.dto.request.NoticeCreateRequest;
import com.rsupport.notice.management.dto.request.NoticePageRequest;
import com.rsupport.notice.management.dto.request.NoticeSearchRequest;
import com.rsupport.notice.management.dto.request.NoticeUpdateRequest;
import com.rsupport.notice.management.dto.response.*;
import com.rsupport.notice.management.exception.CustomException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface NoticeService {

  /**
   * 굥지시항 등록
   *
   * @param request the request
   * @param multipartFileList the multipart file list
   * @return the notice create response
   */
  NoticeCreateResponse createNotice(
      NoticeCreateRequest request, List<MultipartFile> multipartFileList);

  /**
   * 공지사항 수정
   *
   * @param noticeId the notice id
   * @param request the request
   * @param multipartFileList the multipart file list
   * @return the notice update response
   * @throws CustomException the custom exception
   */
  NoticeUpdateResponse updateNotice(
      Long noticeId, NoticeUpdateRequest request, List<MultipartFile> multipartFileList)
      throws CustomException;

  /**
   * 공지사항 삭제
   *
   * @param noticeId the notice id
   * @return the notice delete response
   * @throws CustomException the custom exception
   */
  NoticeDeleteResponse deleteNotice(Long noticeId) throws CustomException;

  /**
   * 공지사항 목록
   *
   * @param request the request
   * @return the notices
   */
  NoticePageResponse getNotices(NoticePageRequest request);

  /**
   * 공지사항 상세
   *
   * @param noticeId the notice id
   * @return the notice
   * @throws CustomException the custom exception
   */
  NoticeDetailResponse getNotice(Long noticeId) throws CustomException;

  /**
   * 공지사항 검색
   *
   * @param request the request
   * @return the notice search response
   */
  NoticeSearchResponse searchNotices(NoticeSearchRequest request);
}
