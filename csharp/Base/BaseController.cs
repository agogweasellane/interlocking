using Interlocking.Controllers;
using Interlocking.Global;
using Interlocking.Models.ServiceLayer;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Bson;
using Newtonsoft.Json;
using System.Net;

namespace Interlocking.Base;


/// <summary>
/// 웹API 컨트롤러용 부모클래스. C#쪽 추상클래스 네이밍룰 좀 고민해봐야 [TO-DO]
/// XxxxAsync()류는 Task가 걸린경우가 많으니 함수명에서 명시
/// 추후 xxxControll이 추가될 경우, 가급적 해당 클래스에.
/// </summary>
public abstract class BaseController<T, RQ, RP>(ILogger<T> logger) : ControllerBase
                where T : ControllerBase
                where RQ : BaseRequestPacket
                where RP : BaseResponsePacket
{
    /// <summary>
    /// 컨트롤러의 로깅에 사용.
    /// </summary>
    protected readonly ILogger<T> _logger = logger;

    /// <summary>
    /// 리스폰스 생성. 다양한 HTTP 상태 코드때문에 IActionResult
    /// </summary>
    protected IActionResult MakeResponse(RP data)
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

    /// <summary>
    /// HttpGet 리퀘스트.
    /// </summary>
    /// <returns>딕셔너리형 리퀘스트</returns>
    public abstract Task<IActionResult> GetAsync([FromQuery] Dictionary<string, object> reqMap);

    /// <summary>
    /// HttpPost 리퀘스트.
    /// </summary>
    /// <param name="req">The request payload.</param>
    /// <returns>BaseRequestPacket 상속받은 리퀘스트</returns>
    public abstract Task<IActionResult> PostAsync(RQ req);


    /// <summary>
    /// HttpPost 리퀘스트.
    /// </summary>
    /// <param name="req"> json스트링 형태 </param>
    /// <param name="files">업로드할 파일[]</param>
    /// <returns>BaseRequestPacket 상속받은 리퀘스트</returns>
    public abstract Task<IActionResult> PostAsync([FromForm(Name = "json")] RQ req,
                                                  [FromForm(Name = RestrictedParam.File)] IFormFile[] files);


    /// <summary>
    /// HttpPut 리퀘스트. 리소스OW
    /// </summary>
    /// <param name="req">The request payload.</param>
    /// <returns>BaseRequestPacket 상속받은 리퀘스트</returns>
    public abstract Task<IActionResult> PutAsync(RQ req);

    /// <summary>
    /// HttpPut 리퀘스트. 리소스OW
    /// </summary>
    /// <param name="req"> json스트링 형태 </param>
    /// <param name="files">업로드할 파일[]</param>
    /// <returns>BaseRequestPacket 상속받은 리퀘스트</returns>
    public abstract Task<IActionResult> PutAsync([FromForm(Name = "json")] RQ req,
                                                 [FromForm(Name = RestrictedParam.File)] IFormFile[] files);

    /// <summary>
    /// HttpPatch 리퀘스트. 리소스 일부update
    /// </summary>
    /// <returns>BaseRequestPacket 상속받은 리퀘스트</returns>
    public abstract Task<IActionResult> PatchAsync(RQ req);

    /// <summary>
    /// HttpPatch 리퀘스트. 리소스 일부update
    /// </summary>
    /// <param name="req"> json스트링 형태 </param>
    /// <param name="files">업로드할 파일[]</param>
    /// <returns>BaseRequestPacket 상속받은 리퀘스트</returns>
    public abstract Task<IActionResult> PatchAsync([FromForm(Name = "json")] RQ req,
                                                   [FromForm(Name = RestrictedParam.File)] IFormFile[] files);

    /// <summary>
    /// HttpDelete 리퀘스트.
    /// </summary>
    /// <param name="req">The request payload.</param>
    /// <returns>BaseRequestPacket 상속받은 리퀘스트</returns>
    public abstract Task<IActionResult> DeleteAsync(RQ req);
}