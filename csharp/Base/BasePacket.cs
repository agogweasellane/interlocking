using Interlocking.Models.ServiceLayer;
using Newtonsoft.Json;
using System.Net;

namespace Interlocking.Base;


public enum MethodEnum
{
    GET,
    POST,
    PATCH,
    PUT,
    DELETE
}
public static class ConvertMethodEnum
{
    private const string GET = "GET";
    private const string POST = "POST";
    private const string PATCH = "PATCH";
    private const string PUT = "PUT";
    private const string DELETE = "DELETE";

    /// <summary>
    /// C# 8.0부터의 간결 switch문과 람다식 활용한 Enum 문자열 변환
    /// </summary>
    /// <param name="methodEnum">MethodEnum => string값</param>
    /// <returns>string형 API 매소드명</returns>
    public static string ToString(MethodEnum methodEnum)
    {
        return methodEnum switch
        {
            MethodEnum.GET => GET,
            MethodEnum.POST => POST,
            MethodEnum.PATCH => PATCH,
            MethodEnum.PUT => PUT,
            MethodEnum.DELETE => DELETE
        };
    }
    public static MethodEnum ToEnum(string method)
    {
        return method switch
        {
            GET => MethodEnum.GET,
            POST => MethodEnum.POST,
            PATCH => MethodEnum.PATCH,
            PUT => MethodEnum.PUT,
            DELETE => MethodEnum.DELETE
        };
    }
}

/// <summary>
/// URL에 파라미터를 넣어서 쓰는 GET, DELETE쪽과는 무관
/// </summary>
public class BaseRequestPacket
{
    public MethodEnum Method{ get; set; }

    public string ToJsonString()
    {
        return JsonConvert.SerializeObject(this);
    }
    public static T ToPacket<T>(string argJson) where T : BaseRequestPacket
    {
        return JsonConvert.DeserializeObject<T>(argJson);
    }
}

/// <summary>
/// 200.OK용 리스폰스와 익셉션용 리스폰스 부모.
/// </summary>
public class BaseResponsePacket
{
    //[JsonPropertyName("statusCode")]
    public HttpStatusCode Status { get; protected set; }

    public string Tag { get; set; }
    public string Code { get; set; }
    public string Message { get; set; }

    public void SetStatus(HttpStatusCode status)
    {
        this.Status = status;// (HttpStatusCode)((int)ResultCode / 1000);
    }

    public string ToJsonString()
    {
        return JsonConvert.SerializeObject(this);
    }
    public static T ToPacket<T>(string jsonString) where T : BaseResponsePacket
    {
        return JsonConvert.DeserializeObject<T>(jsonString);
    }
}
