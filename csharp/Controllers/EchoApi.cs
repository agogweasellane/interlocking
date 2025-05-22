using Interlocking.Base;
using Interlocking.Global;
using Interlocking.Global.Exception;
using Interlocking.Global.WrongException;
using Interlocking.Models.ServiceLayer;
using Microsoft.AspNetCore.Mvc;
using Microsoft.OpenApi.Extensions;
using Swashbuckle.AspNetCore.Annotations;
using Swashbuckle.AspNetCore.Filters;
using System.Net;

namespace Interlocking.Controllers;

/// <summary>
/// http://localhost:5001/apis/echo
/// 연동된 서비스들의 기본동작 및 통신상태 확인용.
/// [SwaggerTag(...)]이 EnableAnnotations옵션하고 충돌남.
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
    private readonly MongoMultiDocumentService _mongoMultiDocService;
    private readonly MariaService _mariaService;

    public EchoController(ILogger<EchoController> logger,
                          RedisService redisService, MariaService mariaService,
                          MongoMultiDocumentService mongoMultiDocumentService) : base(logger)
    {
        _mongoMultiDocService = mongoMultiDocumentService;
        _redisService = redisService;
        _mariaService = mariaService;
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

        ret.MongoHealth = await _mongoMultiDocService.IsAvailableAsync();
        if (ret.MongoHealth == false) { throw new WrongServiceException(HttpStatusCode.ServiceUnavailable, "MongoHealth.IsAvailableAsync==false"); }

        ret.RedisHealth = await _redisService.IsAvailableAsync();
        if (ret.RedisHealth == false) { throw new WrongServiceException(HttpStatusCode.ServiceUnavailable, "RedisHealth.IsAvailableAsync==false"); }

        ret.MariaHealth = await _mariaService.IsAvailableAsync();
        if (ret.MariaHealth == false) { throw new WrongServiceException(HttpStatusCode.ServiceUnavailable, "MariaHealth.IsAvailableAsync==false"); }

        ret.S3Health = false;
        if (ret.S3Health == false) { throw new WrongServiceException(HttpStatusCode.ServiceUnavailable, "S3Health.IsAvailableAsync==false"); }


        return MakeResponse(ret);
    }

    /// <inheritdoc/>
    [HttpPost]
    [SwaggerOperation(Summary = "연동서비스들 목록 체크", Description = "상태가 비정상인 서비스를 false로 입력.")]
    public override async Task<IActionResult> PostAsync(EchoRequest req)
    {
#if DEBUG
        _logger.LogDebug("called.PostAsync {0}", DateTime.Now);
#endif
        EchoResponse ret = new ();
        ret.SetStatus(HttpStatusCode.OK);
        ret.MongoHealth = await _mongoMultiDocService.IsAvailableAsync();
        ret.RedisHealth = await _redisService.IsAvailableAsync();
        ret.MariaHealth = await _mariaService.IsAvailableAsync();
        ret.S3Health = false;
#if DEBUG
        _logger.LogDebug("PostAsync.ret={0}", ret.ToJsonString());
#endif
        return MakeResponse(ret);
    }

    /// <inheritdoc/>
    [HttpPost][Route(RestrictedParam.File)]
    [Consumes(ContextFormat.Json, ContextFormat.FormData)][Produces(ContextFormat.Json)]
    [SwaggerOperation(Summary = "연동서비스들 목록 체크", Description = "상태가 비정상인 서비스는 false로 표기.")]
    public override async Task<IActionResult> PostAsync([FromForm(Name = "json")] EchoRequest req, [FromForm(Name = RestrictedParam.File)] IFormFile[] files)
    {
#if DEBUG
        _logger.LogDebug("called.PostAsync {0}", DateTime.Now);
#endif
        throw new WrongRequestExcpetion();//TO-DO.
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
        ret.MongoHealth = await _mongoMultiDocService.IsAvailableAsync();
        ret.RedisHealth = await _redisService.IsAvailableAsync();
        ret.MariaHealth = await _mariaService.IsAvailableAsync();

        return MakeResponse(ret);
    }

    /// <inheritdoc/>
    [HttpPatch][Route(RestrictedParam.File)]
    [Consumes(ContextFormat.Json, ContextFormat.FormData)][Produces(ContextFormat.Json)]
    [SwaggerOperation(Summary = "연동서비스들 목록 체크", Description = "상태가 비정상인 서비스는 false로 표기./기존 입력값 일부 갱신+파일OW")]
    public override Task<IActionResult> PatchAsync([FromForm(Name = "json")] EchoRequest req, [FromForm(Name = "files")] IFormFile[] files)
    {
#if DEBUG
        _logger.LogDebug("called.PatchAsync {0}", DateTime.Now);
#endif
        throw new WrongRequestExcpetion();//TO-DO.
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
        ret.MongoHealth = await _mongoMultiDocService.IsAvailableAsync();
        ret.RedisHealth = await _redisService.IsAvailableAsync();
        ret.MariaHealth = await _mariaService.IsAvailableAsync();

        return MakeResponse(ret);
    }

    /// <inheritdoc/>
    [HttpPut][Route(RestrictedParam.File)]
    [Consumes(ContextFormat.Json, ContextFormat.FormData)][Produces(ContextFormat.Json)]
    [SwaggerOperation(Summary = "연동서비스들 목록 체크", Description = "상태가 비정상인 서비스는 false로 표기./기존 입력값&파일 전체 OW")]
    public override Task<IActionResult> PutAsync([FromForm(Name = "json")] EchoRequest req, [FromForm(Name = "files")] IFormFile[] files)
    {
#if DEBUG
        _logger.LogDebug("called.PutAsync {0}", DateTime.Now);
#endif
        throw new WrongRequestExcpetion();//TO-DO.
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
        ret.MongoHealth = await _mongoMultiDocService.IsAvailableAsync();
        ret.RedisHealth = await _redisService.IsAvailableAsync();
        ret.MariaHealth = await _mariaService.IsAvailableAsync();

        return MakeResponse(ret);
    }
}

public class EchoRequest : BaseRequestPacket
{
    [SwaggerSchema(Description = "기본 json테스트용")]
    public string? Test { get; set; }
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