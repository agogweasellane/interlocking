using Microsoft.AspNetCore.Mvc;

namespace Interlocking.Base;


/// <summary>
/// MVC패턴 원칙상, 비즈니스 로직은 (최대한)서비스에서 해결해야 하고
/// 클라우드에서는 health체크 처리가 있어야 하니 컨트롤러에서는 서비스들이 정상 작동인지 확인할 기본 구성.
/// 그보다 JAVA쪽과는 달리 BaseXxxxXxx여도 abstract 적용이 유동적인게 일반적이진 않은거 같으니 관련 네이밍룰 보완 필요.
/// </summary>
public abstract class BaseService
{
    protected readonly ILogger<BaseService> _logger;

    protected BaseService(ILogger<BaseService> logger)
    {
        _logger = logger;
    }

    /// <summary>
    /// C#환경에서는 쓸일이 거의 없는 체크함수.
    /// </summary>
    /// <returns>서비스의 사용가능 여부</returns>
    public abstract bool IsAvailable();

    /// <summary>
    /// 비동기식으로 서비스 사용여부 체크.
    /// </summary>
    /// <returns>서비스의 사용가능 여부</returns>
    public abstract Task<bool> IsAvailableAsync();
}