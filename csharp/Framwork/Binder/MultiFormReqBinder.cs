using Interlocking.Base;
using Interlocking.Controllers;
using Interlocking.Global;
using Microsoft.AspNetCore.Mvc.ModelBinding;
using System.Text;
using System.Text.Json;

namespace Interlocking.Framwork.Binder;

public class MultiFormReqBinder<T> : IModelBinder where T : BaseRequestPacket
{
    private readonly ILogger<MultiFormReqBinder<T>> _logger;

    public MultiFormReqBinder(ILogger<MultiFormReqBinder<T>> logger)
    {
        _logger = logger;
    }

    public async Task BindModelAsync(ModelBindingContext bindingContext)
    {
        if (bindingContext == null)
        {
            throw new ArgumentNullException(nameof(bindingContext));
        }
        bindingContext.HttpContext.Request.EnableBuffering(); // 다회 추출할 버퍼링 활성
        var request = bindingContext.HttpContext.Request;
        //string reqBody = await new StreamReader(request.Body, Encoding.UTF8).ReadToEndAsync();
        request.Body.Position = 0;
        _logger.LogDebug("LoggingBinder. FieldName={0}, ModelType.Name={}", bindingContext.FieldName, bindingContext.ModelType.Name);
        _logger.LogDebug("LoggingBinder. ModelType.FullName={0}", bindingContext.ModelType.FullName);


        if(request.Form.Files.Count>0)
        {
            var files = request.Form.Files.ToList();
            bindingContext.Result = ModelBindingResult.Success(files);
            return;
        }
        else
        {
#if DEBUG
            foreach (var key in request.Form.Keys)
            {
                _logger.LogDebug("LoggingBinder. key={0}", key);
            }
#endif
            string? reqJsonValue = request.Form[ContextFormat.BindJson];

            try
            {
                var options = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
                var model = JsonSerializer.Deserialize<T>(reqJsonValue, options);
                bindingContext.Result = ModelBindingResult.Success(model);
                return;
            }
            catch (JsonException ex)
            {
                bindingContext.ModelState.AddModelError(ContextFormat.BindJson, $"JSON 파싱 오류: {ex.Message}");
                bindingContext.Result = ModelBindingResult.Failed();
                return;
            }
        }

        if (!bindingContext.Result.IsModelSet)   {    bindingContext.Result = ModelBindingResult.Failed();    }
    }
}

public class MultiFormFilesBinder : IModelBinder
{
    private readonly ILogger<MultiFormFilesBinder> _logger;

    public MultiFormFilesBinder(ILogger<MultiFormFilesBinder> logger)
    {
        _logger = logger;
    }

    public async Task BindModelAsync(ModelBindingContext bindingContext)
    {
        if (bindingContext == null)
        {
            throw new ArgumentNullException(nameof(bindingContext));
        }

        bindingContext.HttpContext.Request.EnableBuffering(); // 다회 추출할 버퍼링 활성
        var request = bindingContext.HttpContext.Request;
        request.Body.Position = 0;
        _logger.LogDebug("LoggingBinder. FieldName={0}, ModelType.Name={}", bindingContext.FieldName, bindingContext.ModelType.Name);
        _logger.LogDebug("LoggingBinder. ModelType.FullName={0}", bindingContext.ModelType.FullName);


        if (request.Form.Files.Count > 0)
        {
            var files = request.Form.Files.ToList();
            bindingContext.Result = ModelBindingResult.Success(files);
            return;
        }
        else
        {
            return;
        }


        if (!bindingContext.Result.IsModelSet) { bindingContext.Result = ModelBindingResult.Failed(); }
    }
}


public class MultiFormBinderProvider : IModelBinderProvider
{
    /// <inheritdoc/>
    public IModelBinder? GetBinder(ModelBinderProviderContext context)
    {
        if (context == null)
        {
            throw new ArgumentNullException(nameof(context));
        }
        else if (context.BindingInfo.BinderType == null)
        { 
            return null;
        }
        else if (context.Metadata.ModelType == typeof(IList<IFormFile>))
        {
            return null;
        }


        var loggerFactory = context.Services.GetRequiredService<ILoggerFactory>();
        if (context.Metadata.ModelType == typeof(EchoRequest))
        {
            var logger = loggerFactory.CreateLogger<MultiFormReqBinder<EchoRequest>>();
            return new MultiFormReqBinder<EchoRequest>(logger);
        }
        if (context.Metadata.ModelType == typeof(IList<IFormFile>))
        {
            var logger = loggerFactory.CreateLogger<MultiFormFilesBinder>();
            return new MultiFormFilesBinder(logger);
        }


        

        return null;
    }
}