- [주제]
  - 공지사항 관리 REST API 구현
- [기능 요구사항]
  - 공지사항 등록, 수정, 삭제, 조회 API를 구현한다.
  - 공지사항 등록시 입력 항목은 다음과 같다.
    - 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일 (여러개)
  - 공지사항 조회시 응답은 다음과 같다.
    - 목록: 제목, 첨부파일 유무, 등록일시, 조회수, 작성자
    - 상세: 제목, 내용, 등록일시, 조회수, 작성자, 첨부파일
  -  공지사항 검색은 다음과 같다.
    - 검색어: 제목 + 내용, 제목
      - 검색기간: 등록일자
- [비기능 요구사항 및 평가 항목]
  - REST API로 구현
  - 개발 언어는 Java, Kotlin 중 익숙한 개발 언어로 한다.
  - 웹 프레임 워크는 Spring Boot 을 사용한다.
  - Persistence 프레임 워크는 Hibernate 사용시 가산점
  - 데이터 베이스는 제약 없음
  - 기능 및 제약사항에 대한 단위/통합테스트 작성
  - 대용량 트래픽을 고려하여 구현할 것
  - 핵심 문제해결 전략 및 실행 방법등을 README 파일에 명시
---
### 개발환경
- Java 1.8
- Spring Boot 2.4.2
- H2
- Hibernate, JPA
- Redis (캐시 및 조회수 관리)
- Cache (검색 최적화)
### 실행 방법
1. 프로젝트 Clone
2. Redis 설치 
   - window: https://github.com/microsoftarchive/redis
   - mac: brew install redis
3. 프로젝트 실행
### 핵심 문제해결 전략
- **공지사항 등록**
  - 트랜잭션을 활용하여 데이터 무결성 보장
  - @CacheEvict 사용해서 기존 캐시(목록, 검색) 삭제 -> 최신 데이터 유지
- **공지사항 수정**
  - 트랜잭션을 활용하여 데이터 무결성 보장
  - 파일 서버가 분리가 되어 있지 않아 요청에 삭제 리스트와 신규 리스트를 따로 받아 처리
  - @CacheEvict 사용해서 기존 캐시(목록, 검색) 삭제
- **공지사항 삭제**
  - 트랜잭션을 활용하여 데이터 무결성 보장
  - @CacheEvict 사용해서 기존 캐시(목록, 검색) 삭제
- **공지사항 목록**
  - @Cacheable 사용해서 캐시 처리로 조회 속도 개선
    - Key: page_size
  - CacheManager 통해 만료 시간 5분 설정
    - 공지사항 등록, 수정, 삭제로 변동이 발생될 확률이 높으므로
  - 대량 데이터 조회 시 페이징 처리로 DB 부하 감소
- **공지사항 상세**
  - 조회수 증가 Redis 처리
    - 실시간 조회수 증가 시 DB 부하 방지
    - 호출 시 Notice 조회수 + Redis 조회수 응답
    - 1분 단위로 Redis 저장된 조회수 Notice 반영
  - @CachePut 사용하여 조회수 증가 시 캐시 일관성 유지
- **공지사항 검색**
  - @Cacheable 사용해서 캐시 처리로 조회 속도 개선
    - Key: searchType_keyword_startDate_endDate_page_size
  - CacheManager 통해 만료 시간 5분 설정
    - 공지사항 등록, 수정, 삭제로 변동이 발생될 확률이 높으므로
  - 대량 데이터 조회 시 페이징 처리로 DB 부하 감소
  - 인덱스를 '등록일자', '제목', '제목 + 내용'으로 조회 성능 최적화
  - QueryDSL 이용해서 searchType 따른 동적 쿼리로 조회 성능 최적화
### 프로젝트 구조
```
notice-management
├─ .mvn
│  └─ wrapper
│     └─ maven-wrapper.properties
├─ mvnw
├─ mvnw.cmd
├─ pom.xml
├─ README.md
├─ src
│  ├─ main
│  │  ├─ java
│  │  │  └─ com
│  │  │     └─ rsupport
│  │  │        └─ notice
│  │  │           └─ management
│  │  │              ├─ config
│  │  │              │  ├─ CacheConfig.java
│  │  │              │  ├─ FilterConfig.java
│  │  │              │  ├─ QuerydslConfig.java
│  │  │              │  └─ RedisConfig.java
│  │  │              ├─ controller
│  │  │              │  ├─ AttachmentController.java
│  │  │              │  └─ NoticeController.java
│  │  │              ├─ dto
│  │  │              │  ├─ common
│  │  │              │  │  ├─ AttachmentDto.java
│  │  │              │  │  ├─ CommonResponse.java
│  │  │              │  │  └─ NoticeDto.java
│  │  │              │  ├─ request
│  │  │              │  │  ├─ AttachmentDownloadRequest.java
│  │  │              │  │  ├─ NoticeCreateRequest.java
│  │  │              │  │  ├─ NoticePageRequest.java
│  │  │              │  │  ├─ NoticeSearchRequest.java
│  │  │              │  │  └─ NoticeUpdateRequest.java
│  │  │              │  └─ response
│  │  │              │     ├─ NoticeCreateResponse.java
│  │  │              │     ├─ NoticeDeleteResponse.java
│  │  │              │     ├─ NoticeDetailResponse.java
│  │  │              │     ├─ NoticePageResponse.java
│  │  │              │     ├─ NoticeSearchResponse.java
│  │  │              │     └─ NoticeUpdateResponse.java
│  │  │              ├─ entity
│  │  │              │  ├─ Attachment.java
│  │  │              │  └─ Notice.java
│  │  │              ├─ exception
│  │  │              │  ├─ CustomException.java
│  │  │              │  ├─ ErrorCode.java
│  │  │              │  └─ GlobalExceptionHandler.java
│  │  │              ├─ filter
│  │  │              │  └─ LoggingFilter.java
│  │  │              ├─ NoticeManagementApplication.java
│  │  │              ├─ redis
│  │  │              │  └─ NoticeViewCountService.java
│  │  │              ├─ repository
│  │  │              │  ├─ AttachmentRepository.java
│  │  │              │  ├─ NoticeRepository.java
│  │  │              │  ├─ NoticeRepositoryCustom.java
│  │  │              │  └─ NoticeRepositoryImpl.java
│  │  │              ├─ scheduler
│  │  │              │  └─ NoticeViewCountBatchService.java
│  │  │              ├─ service
│  │  │              │  ├─ AttachmentService.java
│  │  │              │  ├─ AttachmentServiceImpl.java
│  │  │              │  ├─ NoticeService.java
│  │  │              │  └─ NoticeServiceImpl.java
│  │  │              └─ utils
│  │  │                 └─ NoticeUtil.java
│  │  └─ resources
│  │     ├─ application.yml
│  │     ├─ sql
│  │     │  └─ data.sql
│  │     └─ upload
│  └─ test
│     └─ java
│        └─ com
│           └─ rsupport
│              └─ notice
│                 └─ management
│                    ├─ controller
│                    │  ├─ AttachmentControllerTest.java
│                    │  └─ NoticeControllerTest.java
│                    ├─ NoticeManagementApplicationTests.java
│                    └─ service
│                       ├─ AttachmentServiceImplTest.java
│                       └─ NoticeServiceImplTest.java
└─ upload
   ├─ 2aab363a-40f9-4ea3-bdcc-493690320869.jpg
   ├─ 7e9ca34c-d382-4f5b-9e96-332d89136594.jpg
   └─ c9300a3c-1a97-4d22-8eec-d3cf2a5ff48c.jpg

```

