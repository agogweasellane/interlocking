using Interlocking.Base;
using Interlocking.Global.WrongException;
using Swashbuckle.AspNetCore.Annotations;
using Swashbuckle.AspNetCore.Filters;
using System.Net;


namespace Interlocking.Global.Exception;

public class WrongExceptionResponse : BaseResponsePacket
{
}

public class GlobalExceptionResponseExample : IExamplesProvider<WrongExceptionResponse>
{
    WrongExceptionResponse IExamplesProvider<WrongExceptionResponse>.GetExamples()
    {
        WrongExceptionResponse ret = new();
        ret.SetResultCode(HttpStatusCode.InternalServerError);
        ret.Message = "(StatusCode=익셉션 클래스) 400=WrongRequestExcpetion, 503=WrongServiceException, 500=WrongException";

        return ret;
    }
}