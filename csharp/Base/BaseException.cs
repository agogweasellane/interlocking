using Interlocking.Controllers;

using Swashbuckle.AspNetCore.Annotations;
using System.Net;

namespace Interlocking.Base;

/// <summary>
/// HttpStatusCode.ServiceUnavailable(503)      서버 리소스쪽 일시적 불능(예. 부팅등 해당 리소스가 가동되기전)
/// HttpStatusCode.InternalServerError(500)     서버사이트코드 에러 OR 트래픽폭발등 인프라쪽 지원 필요
/// [수정.2025-05-18]
/// </summary>
public class BaseException : Exception
{
    [SwaggerSchema(Description = "라이브때도 출력된 필드[1/2]  ServiceUnavailable(503)-서버 리소스쪽 일시적 불능(예. 부팅등 해당 리소스가 가동X)")]
    public HttpStatusCode StatusCode { get; protected set; }

    [SwaggerSchema(Description = "라이브때도 출력될 필드[2/2]  오류 메시지")]
    public string Message { get; set; }


    public BaseException(HttpStatusCode code, string? message = null)
    {
        StatusCode = code;
        Message = message;
    }
}