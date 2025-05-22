using Interlocking.Models.ServiceLayer;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Bson;
using Newtonsoft.Json;
using System.Net;

namespace Interlocking.Base;


/// <summary>
/// 웹API 컨트롤러용 부모클래스. C#쪽 추상클래스 네이밍룰 좀 고민해봐야 [TO-DO]
/// XxxxAsync()류는 Task가 걸린경우가 많으니 함수명에서 명시
/// </summary>
public abstract class BaseController<T> : ControllerBase where T : ControllerBase
{
    protected readonly ILogger<T> _logger;
    //protected readonly RedisService _redisService;

    protected BaseController(ILogger<T> logger)
    {
        _logger = logger;
    }

    /// <summary>
    /// 리스폰스 생성. 다양한 HTTP 상태 코드때문에 IActionResult
    /// </summary>
    protected IActionResult MakeResponse<R>(R data) where R : BaseResponsePacket
    {//MEMO. 다양한 HTTP 상태 코드때문에 IActionResult
        IActionResult ret = null;
        if (data.Status == 0)
        {
            ret = StatusCode((int)HttpStatusCode.InternalServerError, data);
        }
        else
        {
            ret = StatusCode((int)data.Status, data);
        }
#if DEBUG
        _logger.LogDebug("MakeResponse.data={0}", data.ToJsonString());
#endif

        //return Task.FromResult<IActionResult>(ret);
        return ret;
    }

    [HttpGet] public abstract Task<IActionResult> GetAsync();

    [HttpPost] public abstract Task<IActionResult> PostAsync();

    [HttpPut] public abstract Task<IActionResult> PutAsync();

    [HttpDelete] public abstract Task<IActionResult> DeleteAsync();

    [HttpPatch] public abstract Task<IActionResult> PatchAsync();
}