using Newtonsoft.Json;
using System.Net;

namespace Interlocking.Base;


/// <summary>
/// URL에 파라미터를 넣어서 쓰는 GET, DELETE쪽과는 무관
/// </summary>
public class BaseRequestPacket
{
    public DateOnly Date { get; protected set; }
    public string Hash { get; set; }

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

    public void SetResultCode(HttpStatusCode status)
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
