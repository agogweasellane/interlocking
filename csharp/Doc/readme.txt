코드 컨벤션
- JAVA하고 달리 프로퍼티는 대문자로 시작되는거 주의.

리퀘스트/리스폰스
- 변수 Xxxx		프로퍼티 변수

비동기 관련
	JAVA의 Callable<V>과 Future<V>중 어느쪽이 Task<V>랑 가까운편일까 했는데 비동기 작업 '결과반환'은 Future

코드 고도화 및 구조잡기
	(Base)Async()에서 (Base)abstrat AsyncControll()를 호출시키고, 자식에서 재정의하기에는 어트리뷰트 지정등에서 경로가 안 맞는 문제. 그런고로 (Base)abstrat Async()
------------------------------------------------------------
------------------------------------------------------------
아키텍쳐 및 클린코드관련
ASP 닷넷코어
	https://learn.microsoft.com/ko-kr/aspnet/core/tutorials/first-web-api?view=aspnetcore-9.0&tabs=visual-studio
	https://learn.microsoft.com/ko-kr/aspnet/core/mvc/models/file-uploads?view=aspnetcore-9.0
	(2023) https://mycsharpdeveloper.wordpress.com/2023/07/03/how-to-upload-files-in-asp-net-core-webapi-using-csharp-and-change-file-size-limit/  //말레이시아

베스트 샘플
	(2025) https://codewithmukesh.com/blog/restful-api-best-practices-for-dotnet-developers/
	(2025) https://learn.microsoft.com/en-us/azure/architecture/best-practices/api-design
바인더
	(2025) https://learn.microsoft.com/ko-kr/aspnet/core/mvc/advanced/custom-model-binding?view=aspnetcore-9.0
	(2018) https://www.palmmedia.de/Blog/2018/5/13/aspnet-core-model-binding-of-abstract-classes
	(2024) https://thetechplatform.medium.com/custom-model-binding-in-asp-net-core-a-comprehensive-guide-7f50f6de6986
------------------------------------------------------------
------------------------------------------------------------
연동
maraiDB		https://blog.georgekosmidis.net/using-mariadb-in-an-aspnet-core-api-with-entity-framework-core.html
스웨거
	https://learn.microsoft.com/ko-kr/aspnet/core/fundamentals/openapi/overview?view=aspnetcore-9.0&preserve-view=true
	https://learn.microsoft.com/ko-kr/aspnet/core/tutorials/getting-started-with-swashbuckle?view=aspnetcore-8.0&tabs=visual-studio
	https://github.com/domaindrivendev/Swashbuckle.AspNetCore
	(2020) https://medium.com/trimble-maps-engineering-blog/customizing-swagger-responses-for-better-api-documentation-affbfafeac8f
S3
	https://docs.aws.amazon.com/ko_kr/sdk-for-net/v3/developer-guide/csharp_s3_code_examples.html
	(2025) https://codewithmukesh.com/blog/working-with-aws-s3-using-aspnet-core/

	(실무당시 참조) https://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region 
    (실무당시 참조) http://www.codelocker.net/30385/dot-net-c-sharp-upload-a-file-to-amazon-aws-s3-with-dot-net-and-c-sharp/	(404)
	(실무당시 참조) https://stackoverflow.com/questions/33545065/amazon-s3-how-to-get-a-list-of-folders-in-the-bucket
로깅
	(2024) https://www.sysnet.pe.kr/2/0/13520#google_vignette

코드규칙
	https://learn.microsoft.com/ko-kr/aspnet/core/tutorials/first-web-api?view=aspnetcore-9.0&tabs=visual-studio
	https://gemini.google.com/u/1/gem/coding-partner/
기타 가이드북
	https://learn.microsoft.com/ko-kr/dotnet/csharp/fundamentals/coding-style/coding-conventions
	https://learn.microsoft.com/ko-kr/aspnet/core/security/?view=aspnetcore-9.0
보안
	디도스대책 System.Text.RegularExpressions
------------------------------------------------------------
------------------------------------------------------------
외부툴

Robo3T
db.tbl_echo.createIndex( { "ExpireAt.DateTime": 1 }, { expireAfterSeconds: 0 } );
db.tbl_echo.createIndex( { "ExpireAt": 1 }, { expireAfterSeconds: 0 } );
------------------------------------------------------------
------------------------------------------------------------

빌드시간
[+] Building 34.8s (15/15) FINISHED		최초 빌드 성공
[+] Building 10.7s (15/15) FINISHED		반복 빌드시.

docker build -t interlocking_dev .
docker run -d -p 5001:5001 --network nw-springboot --detach --name interlocking_dev interlocking_dev


docker build -t interlocking_dev --target final_dev .
docker run -d -p 5001:5001 --name interlocking_dev interlocking_dev



라이브용.
docker build -t interlocking_release --target final .
docker run -d -p 5001:5001 --name interlocking_release interlocking_release






------------------------------------------------------------
docker buildx version
docker buildx update


기본 빌더 변경 (고급): 현재 사용 중인 빌더에 문제가 있을 수 있습니다. 기본 빌더를 변경해 볼 수 있습니다.

docker buildx create --use
docker buildx inspect --bootstrap
------------------------------------------------------------











확인해야 할 항목 및 고려 사항:

프레임워크: .NET 9.0 (표준 용어 지원)이 선택되어 있습니다. 이는 현재 최신 버전이므로 특별히 문제가 되지는 않습니다. 다만, 특정 환경이나 요구 사항으로 인해 이전 버전(.NET 8 LTS 등)을 사용해야 한다면 해당 버전을 선택해야 합니다. 현재로서는 괜찮습니다.

인증 유형: 없음이 선택되어 있습니다. API에 인증이 필요하다면 (예: JWT Bearer, Azure AD 등) 해당 유형을 선택하고 추가적인 설정을 진행해야 합니다. 현재 샘플 테스트 단계이므로 없음으로 진행해도 무방합니다.

HTTPS에 대한 구성: 체크되어 있습니다. HTTPS를 사용하는 것이 보안상 권장되지만, 로컬 개발 및 테스트 환경에서는 HTTP로 진행하고 싶을 수도 있습니다. HTTP로만 테스트하려면 체크를 해제할 수 있습니다. 체크된 상태로 진행하면 https://localhost:<포트>로 접근해야 하며, 개발 인증서 관련 경고가 표시될 수 있습니다.

컨테이너 지원 사용: 체크되어 있습니다. Docker 컨테이너를 사용하여 애플리케이션을 배포할 계획이라면 이 옵션을 유지하는 것이 좋습니다. Dockerfile이 자동으로 생성됩니다. 로컬에서만 테스트할 계획이라면 체크를 해제해도 무방합니다.

컨테이너 OS: Linux가 선택되어 있습니다. 컨테이너를 Linux 기반으로 빌드합니다. Windows 컨테이너를 사용하려면 Windows를 선택해야 합니다. 배포 환경에 따라 선택하시면 됩니다. 로컬 테스트에는 큰 영향이 없습니다.

컨테이너 빌드 형식: Dockerfile이 선택되어 있습니다. Docker 이미지를 빌드하는 표준 방식입니다. 특별한 이유가 없다면 이 설정을 유지합니다.

OpenAPI 지원 사용: 체크되어 있습니다. Swagger/OpenAPI를 사용하여 API 문서를 자동으로 생성하고 테스트할 수 있도록 지원합니다. API 개발에 매우 유용한 기능이므로 유지하는 것을 권장합니다.

최상위 문 사용 안 함: 체크되어 있습니다. Program.cs 파일이 기존의 클래스 및 Main 메서드 형태로 생성됩니다. 체크를 해제하면 C# 9.0의 최상위 문법으로 간결하게 작성됩니다. 개인적인 선호도에 따라 선택하시면 됩니다.

컨트롤러 사용: 체크되어 있습니다. 웹 API 개발을 위해 컨트롤러를 사용하므로 반드시 체크해야 합니다.

.NET Aspire 오케스트레이션에 등록: 체크 안 되어 있습니다. .NET Aspire는 분산 애플리케이션 개발을 위한 프레임워크입니다. 현재 간단한 API 테스트 목적이라면 체크하지 않아도 됩니다.

결론적으로, 현재 선택된 항목들은 일반적인 ASP.NET Core 웹 API 개발 환경에 적합해 보입니다. 특별히 변경해야 할 부분은 없지만, HTTPS 사용 여부나 컨테이너 지원 필요 여부에 따라 체크 박스를 조정할 수 있습니다.

