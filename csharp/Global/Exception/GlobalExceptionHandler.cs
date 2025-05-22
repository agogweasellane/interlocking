using Interlocking.Base;
using Interlocking.Global.WrongException;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using MongoDB.Bson;
using Newtonsoft.Json;
using System;
using System.Net;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace Interlocking.Global.Exception;


/// <summary>
/// GlobalException이 클린코드 방식중 가장 가독성 향상에 좋아서 사용중인데,
/// JAVA하고 달리 특정 익셉션 클래스만 취급하도록 옵션 거는것이 어트리뷰트에서 안 되서 아쉽다.
/// </summary>
public class GlobalExceptionHandler : IExceptionFilter
{
    private readonly ILogger<GlobalExceptionHandler> _logger;
    private readonly IWebHostEnvironment _environment;

    public GlobalExceptionHandler(ILogger<GlobalExceptionHandler> logger, IWebHostEnvironment environment)
    {
        _logger = logger;
        _environment = environment;
    }

    public void OnException(ExceptionContext context)
    {
        if (context.ExceptionHandled)
        {
            return;
        }

        BaseException tmpExcep = null;

        if (context.Exception is WrongRequestExcpetion ex1)        {    tmpExcep = ex1;    }
        else if (context.Exception is WrongControllerException ex2){    tmpExcep = ex2;    }
        else if (context.Exception is WrongServiceException ex3)   {    tmpExcep = ex3;   }
        else
        {
            tmpExcep = new BaseException(HttpStatusCode.InternalServerError, "unknown error");
        }
        _logger.LogError("[path:{0}] {1} - {2}",
                        context.HttpContext.Request.Path, tmpExcep.GetType().Name, tmpExcep.Message);
        _logger.LogError("[path:{0}] {1} = {2}",
                        context.HttpContext.Request.Path, tmpExcep.GetType().Name, context.Exception.Message);

        WrongExceptionResponse ret = new();
        ret.SetResultCode(tmpExcep.StatusCode);
        ret.Tag = tmpExcep.GetType().Name;
#if DEBUG
        _logger.LogDebug("tmpExcep={0}", ret.ToJsonString());
#endif


        if (_environment.IsDevelopment())
        {
            ProblemDetails problemDetails = new();
            problemDetails.Status = (int)tmpExcep.StatusCode;
            problemDetails.Title = tmpExcep.GetType().Name;
            problemDetails.Detail = "("+context.Exception.Message+") " + tmpExcep.Message;
            problemDetails.Instance = context.HttpContext.Request.Path;

            context.Result = new ObjectResult(problemDetails)
            {
                StatusCode = (int)tmpExcep.StatusCode
            };
        }
        else
        {
            context.Result = new ContentResult
            {
                StatusCode = (int)ret.Status,
                ContentType = ContextFormat.Json,
                Content = ret.ToJsonString()
            };
        }

        context.ExceptionHandled = true;
        _logger.LogError(context.Exception, "OnException: {ExceptionType}", context.Exception.GetType().Name);
    }
}