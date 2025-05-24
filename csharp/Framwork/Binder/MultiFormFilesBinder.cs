using Microsoft.AspNetCore.Mvc.ModelBinding;

namespace Interlocking.Framwork.Binder;



public class MultiFormFilesBinderProvider : IModelBinderProvider
{
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



        if (context.Metadata.ModelType != typeof(IList<IFormFile>))
        {
            return null;
        }


        var loggerFactory = context.Services.GetRequiredService<ILoggerFactory>();
        var logger = loggerFactory.CreateLogger<MultiFormFilesBinder>();
        return new MultiFormFilesBinder(logger);
    }
}