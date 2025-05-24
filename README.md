# 1. JAVA
### 0) Ver. 0.9.2505
### 1) 프레임워크
- springBoot 3.4.5
- JDK 17
- MAVEN
- 필터
    - CachingFilter: 이후에 패킷 압축해제/복호화같은 부가기능 구현을 위한 선행과정.
    - AuthFilter: request바디가 있는 호출METHOD별 필수 입력항목 체크 (현재는 json 필드값과 호출METHOD 비교로 예시)
### 2) restFull API
- 제네릭&추상클래스를 통한 API method 규칙 적용
- apis/simple
    - 별도의 연동없이 해당 웹API서버의 기본 구동 여부 확인용.
- apis/ehco
    - 연동된 시스템의 정상적인 작동을 체크. 로드밸런서의 healthCheck와 관리툴 메뉴등에서 활용가능.
### 3) SQL
- 마리아DB: mariadb.jdbc
    - QueryDSL이 2024년 1월 이후로 업데이트가 중단되서 대체재로 JOOQ 적용 고려중.
### 4) NoSQL
- 레디스: io.lettuce 6.5.2.RELEASE
- 몽고DB
### 5) 클라우드 스토리지
- AWS S3버킷
    - public/private용 버킷 정책(s3설정관련.txt)에 맞춘 파일 업로드로 버킷 보안관련 최신사양 준수.
    - 파일 확인/삭제 가능
### 6) 기타
- GlobalExceptionHandler
    - 글로벌익셉션 처리를 통해 익셉션관련 클린코드 적용.
    - 적용 테스트시 기존 코드길이가 342줄에서 200줄로 약 41% 감소 (BaseRestController 기준)
- lombok을 통한 빌더패턴 활용
- multiform 첨부파일 처리가능


# 2. C#
### 0) Ver. 0.8.2505
### 1) 프레임워크
- ASP .netCore (.net 9.0)
    - web API
    - MultiFormReqBinder: 멀티폼 API내에서 매핑처리
### 2) restFull API
- 제네릭&추상클래스를 통한 API method 규칙 적용
- apis/simple
    - 별도의 연동없이 해당 웹API서버의 기본 구동 여부 확인용.
- apis/ehco
    - json용 함수: 리퀘스트 바디 처리 가능
    - multiform형 함수
          - 바인더를 통해 restFull API 매소드 체크후, json에 입력. 컨트롤러에서 매소드에 맞게 분기처리.
          - API 호출도구에 따른 입력값 차이(스웨거, 포스트맨 기준). 클라이언트 구현후 통신테스트를 위한 추가 디버깅 필요.
### 3) SQL
- 마리아DB: MongoDB.Driver 3.4.0
### 4) NoSQL
- 레디스: StackExchange.Redis 2.8.37
- 몽고DB: MongoDB.Driver 3.4.0
### 5) 클라우드 스토리지
- AWS S3버킷
    - AWSSDK.S3 3.7.416.31
    - AWSSDK.Extensions.NETCore.Setup 3.7.400
    - 계정 액세스키를 통한 기본 연동확인.
### 6) 기타
- GlobalExceptionHandler: 글로벌익셉션 처리를 통해 익셉션관련 클린코드 적용.
- 레디스/몽고/마리아DB 적용 및 도커 빌드&배포에 약 2일
    - 기본 설정/구현/기본동작 테스트
    - ASP 닷넷 코어용으로 전부 첫시도.
- 스웨거: debug/live 빌드에 따른 문서페이지 차단 제어 적용
- 도커: 파워쉘 파일(*.ps1)을 이용한 원클릭 자동 빌드+배포
- [🔗 C#용 코딩 규칙](https://google.com](https://learn.microsoft.com/ko-kr/dotnet/csharp/fundamentals/coding-style/coding-conventions, "ms link")에 맞춰 리팩토링 상시 진행중.
