using Interlocking.Base;
using Interlocking.Framwork.Binder;
using Interlocking.Global;
using Interlocking.Global.Exception;
using Interlocking.Global.WrongException;
using Interlocking.Models.ServiceLayer;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Hosting;
using Microsoft.OpenApi.Extensions;
using Swashbuckle.AspNetCore.Annotations;
using Swashbuckle.AspNetCore.Filters;
using System.Net;
using System.Xml.Linq;

namespace Interlocking.Controllers;

/// <summary>
/// http://localhost:5001/apis/echo
/// 연동된 서비스들의 기본동작 및 통신상태 확인용.
/// [SwaggerTag(...)]이 EnableAnnotations옵션하고 충돌남.
/// 클래스를 명시하는 위치에서 생성자 처리가 가능한 C#의 문법이 있어서 임시로 써보는데
/// 이게 과연 보편적일까? 세부 산업에 따라 또 다를테고.
/// </summary>
[ApiController]
[Route("apis/echo")]
[ProducesResponseType(StatusCodes.Status200OK, Type = typeof(EchoResponse))]
[ProducesResponseType(StatusCodes.Status400BadRequest, Type = typeof(WrongExceptionResponse))]
[ProducesResponseType(StatusCodes.Status500InternalServerError, Type = typeof(WrongExceptionResponse))]
[Produces("application/json")]
public class EchoController : BaseController<EchoController, EchoRequest, EchoResponse>
{
    private readonly RedisService _redisService;
    private readonly MongoMultiDocumentService _mongoService;
    private readonly MariaService _mariaService;
    private readonly AwsS3Service _awsS3Service;

    public EchoController(RedisService redisService, MariaService mariaService, MongoMultiDocumentService mongoService,
                          IAwsS3Service awsS3Service,
                          ILogger<EchoController> logger) : base(logger)
    {
        _mongoService = mongoService;
        _redisService = redisService;
        _mariaService = mariaService;
#pragma warning disable CS8601 // 가능한 null 참조
        _awsS3Service = (AwsS3Service?)awsS3Service;//WARN. System.InvalidOperationException: Unable to resolve service for type
#pragma warning restore CS8601 // 가능한 null 참조
    }




    /// <inheritdoc/>
    [HttpPost]
    [HttpPatch]
    [HttpPut]
    [Route(RestrictedParam.File)]
    [Consumes(ContextFormat.FormData, ContextFormat.Json)]
    [Produces(ContextFormat.Json)]
    [SwaggerOperation(Summary = "연동서비스들 목록 체크", Description = "상태가 비정상인 서비스는 false로 표기.")]
    public override async Task<IActionResult> MultiformAsync([ModelBinder(typeof(MultiFormReqBinder<BaseRequestPacket>), Name = "json")] EchoRequest req, 
                                                             IList<IFormFile> files)
    {
#if DEBUG
        _logger.LogDebug("called.PostAsync {0}", DateTime.Now);
        _logger.LogDebug("PostAsync.req={0}", req.ToJsonString());
#endif
        //IActionResult ret = req.Method switch
        //{//enum쪽 변환에서는 유용했는데 컨트롤러단에서도 괜찮을지는...
        //    MethodEnum.POST => await PostAsync(req) as EchoResponse,
        //    MethodEnum.PATCH => await PatchAsync(req) as EchoResponse,
        //    MethodEnum.PUT => await PutAsync(req) as EchoResponse
        //};
        IActionResult ret = null;

        if (files != null)
        {
            req.Files = files.ToArray();
        }

        if (MethodEnum.POST == req.Method)      { ret = await PostAsync(req); }
        else if (MethodEnum.PUT == req.Method)  { ret = await PutAsync(req); }
        else if (MethodEnum.PATCH == req.Method){ ret = await PatchAsync(req); }


        return ret;
    }


    /// <inheritdoc/>
    [HttpGet]
    [SwaggerOperation(Summary = "health체크용", Description = "(클라우드)로드밸런서내 인스턴스의 health체크에 활용.")]
    public override async Task<IActionResult> GetAsync([FromQuery] Dictionary<string, object> reqMap)
    {
#if DEBUG
        _logger.LogDebug("called.GetAsync {0}", DateTime.Now);
#endif
        EchoResponse ret = new();
        ret.SetStatus(HttpStatusCode.OK);

        ret.MongoHealth = await _mongoService.IsAvailableAsync();
        if (ret.MongoHealth == false) { throw new WrongServiceException(HttpStatusCode.ServiceUnavailable, "MongoHealth.IsAvailableAsync==false"); }

        ret.RedisHealth = await _redisService.IsAvailableAsync();
        if (ret.RedisHealth == false) { throw new WrongServiceException(HttpStatusCode.ServiceUnavailable, "RedisHealth.IsAvailableAsync==false"); }

        ret.MariaHealth = await _mariaService.IsAvailableAsync();
        if (ret.MariaHealth == false) { throw new WrongServiceException(HttpStatusCode.ServiceUnavailable, "MariaHealth.IsAvailableAsync==false"); }

        ret.S3Health = await _awsS3Service.IsAvailableAsync();
        if (ret.S3Health == false) { throw new WrongServiceException(HttpStatusCode.ServiceUnavailable, "S3Health.IsAvailableAsync==false"); }


        return MakeResponse(ret);
    }

    /// <inheritdoc/>
    [HttpPost]
    [Consumes(ContextFormat.Json)]
    [SwaggerOperation(Summary = "연동서비스들 목록 체크", Description = "상태가 비정상인 서비스를 false로 입력.")]
    public override async Task<IActionResult> PostAsync([FromBody] EchoRequest req)
    {
#if DEBUG
        _logger.LogDebug("called.PostAsync {0}", DateTime.Now);
#endif
        EchoResponse ret = new ();
        ret.SetStatus(HttpStatusCode.OK);
        ret.MongoHealth = await _mongoService.IsAvailableAsync();
        ret.RedisHealth = await _redisService.IsAvailableAsync();
        ret.MariaHealth = await _mariaService.IsAvailableAsync();
        ret.S3Health = await _awsS3Service.IsAvailableAsync();
#if DEBUG
        _logger.LogDebug("PostAsync.ret={0}", ret.ToJsonString());
#endif


        return MakeResponse(ret);
    }


    /// <inheritdoc/>
    [HttpPatch]
    [SwaggerOperation(Summary = "연동서비스들 목록 체크", Description = "상태가 비정상인 서비스는 false로 표기./기존 입력값 일부 갱신")]
    public override async Task<IActionResult> PatchAsync(EchoRequest req)
    {
#if DEBUG
        _logger.LogDebug("called.PatchAsync {0}", DateTime.Now);
#endif
        EchoResponse ret = new();
        ret.SetStatus(HttpStatusCode.OK);
        ret.MongoHealth = await _mongoService.IsAvailableAsync();
        ret.RedisHealth = await _redisService.IsAvailableAsync();
        ret.MariaHealth = await _mariaService.IsAvailableAsync();
        ret.S3Health = await _awsS3Service.IsAvailableAsync();


        return MakeResponse(ret);
    }


    /// <inheritdoc/>
    [HttpPut]
    [SwaggerOperation(Summary = "연동서비스들 목록 체크", Description = "상태가 비정상인 서비스는 false로 표기./기존 입력값 전체 OW")]
    public override async Task<IActionResult> PutAsync(EchoRequest req)
    {
#if DEBUG
        _logger.LogDebug("called.PutAsync {0}", DateTime.Now);
#endif
        EchoResponse ret = new();
        ret.SetStatus(HttpStatusCode.OK);
        ret.MongoHealth = await _mongoService.IsAvailableAsync();
        ret.RedisHealth = await _redisService.IsAvailableAsync();
        ret.MariaHealth = await _mariaService.IsAvailableAsync();
        ret.S3Health = await _awsS3Service.IsAvailableAsync();


        return MakeResponse(ret);
    }

    /// <inheritdoc/>
    [HttpDelete]
    [SwaggerResponse(StatusCodes.Status200OK, Type = typeof(EchoResponse), Description = "연동된 모든 서비스가 정상작동 / 테스트용 테이블&보드 clearAll")]
    public override async Task<IActionResult> DeleteAsync(EchoRequest req)
    {
#if DEBUG
        _logger.LogDebug("called.DeleteAsync {0}", DateTime.Now);
#endif

        EchoResponse ret = new();
        ret.SetStatus(HttpStatusCode.OK);
        ret.MongoHealth = await _mongoService.IsAvailableAsync();
        ret.RedisHealth = await _redisService.IsAvailableAsync();
        ret.MariaHealth = await _mariaService.IsAvailableAsync();
        ret.S3Health = await _awsS3Service.IsAvailableAsync();


        return MakeResponse(ret);
    }
}

/// <summary>
/// 기본 JSON 테스트
/// </summary>
public class EchoRequest : BaseRequestPacket
{
    /// <summary>
    /// 테스트용 필드.
    /// </summary>
    [SwaggerSchema(Description = "기본 json테스트용")]
    public string? Test { get; set; }

    public IFormFile[]? Files { get; set; }
}

/// <summary>
/// EchoResponse 클래스는 연동된 서비스들의 상태를 나타내는 응답 모델입니다.
/// </summary>
public class EchoResponse : BaseResponsePacket
{
    /// <summary>
    /// 레디스 연결 및 기본동작 정상여부
    /// </summary>
    [SwaggerSchema(Description = "레디스 연결 및 기본동작 정상여부")]
    public bool RedisHealth { get; set; }

    /// <summary>
    /// MariaDB 연결 및 기본동작 정상여부
    /// </summary>
    [SwaggerSchema(Description = "MariaDB 연결 및 기본동작 정상여부")]
    public bool MariaHealth { get; set; }

    /// <summary>
    /// MongoDB 연결 및 기본동작 정상여부
    /// </summary>
    [SwaggerSchema(Description = "MongoDB 연결 및 기본동작 정상여부")]
    public bool MongoHealth { get; set; }

    /// <summary>
    /// (TO-DO)스토리지-S3 연결 및 기본동작 정상여부
    /// </summary>
    [SwaggerSchema(Description = "(TO-DO)스토리지-S3 연결 및 기본동작 정상여부")]
    public bool S3Health { get; set; }

    /// <summary>
    /// (선택사항) 파일 업로드 및 조회관련
    /// </summary>
    [SwaggerSchema(Description = "(선택사항) 파일 업로드 및 조회관련")]
    public IFormFile[]? Files { get; set; }
}

/// <summary>
/// apis/echo 서버처리가 200.OK일때 기준 예시.
/// </summary>
public class EchoResponseExample : IExamplesProvider<EchoResponse>
{
    EchoResponse IExamplesProvider<EchoResponse>.GetExamples()
    {
        EchoResponse ret = new();
        ret.SetStatus(HttpStatusCode.OK);
        ret.Tag = HttpStatusCode.OK.GetDisplayName();
        ret.Code = HttpStatusCode.OK.GetDisplayName();
        ret.Message = null;
        ret.RedisHealth = true;
        ret.MariaHealth = true;
        ret.MongoHealth = true;
        ret.S3Health = true;
        ret.Files = null;

        return ret;
    }
}