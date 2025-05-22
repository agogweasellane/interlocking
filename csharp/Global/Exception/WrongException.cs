using Interlocking.Base;
using Swashbuckle.AspNetCore.Filters;
using System.Net;

namespace Interlocking.Global.WrongException;

public class WrongException : BaseException
{//별도로 정
    public WrongException(HttpStatusCode code = HttpStatusCode.InternalServerError, string? message = null) : base(code, message)
    {
    }
}
public class WrongRequestExcpetion : BaseException
{
    public WrongRequestExcpetion(HttpStatusCode code=HttpStatusCode.BadRequest, string? message = null) : base(code, message)
    {
    }
}

public class WrongControllerException : BaseException
{
    public WrongControllerException(HttpStatusCode code = HttpStatusCode.InternalServerError, string? message = null) : base(code, message)
    {
    }
}

public class WrongServiceException : BaseException
{
    public WrongServiceException(HttpStatusCode code = HttpStatusCode.ServiceUnavailable, string? message = null) : base(code, message)
    {
    }
}

