namespace Interlocking.Global;

/// <summary>
/// 상수 값 정의
/// </summary>
public static class ConstValue
{
    /// <summary>
    /// 1주일 TTL(초 단위)
    /// </summary>
    public const int TtlTermWeek1 = 7 * 24 * 3600;

    /// <summary>
    /// 1일 TTL(초 단위)
    /// </summary>
    public const int TtlTermDay1 = 1 * 24 * 3600;

    /// <summary>
    /// 5분 TTL(초 단위)
    /// </summary>
    public const int TtlTermMin5 = 5 * 60;

    /// <summary>
    /// UTC 오프셋
    /// </summary>
    public const int UTC_OFFSET = 9;
}

public static class RestrictedParam
{
    public const string File = "files";
}

/// <summary>
/// 레디스 보드 보드명.
/// </summary>
public static class RedisBoard
{
    public const string Fake = "fake";

    public const string Echo = "board_echo";//레디스 서버 체크용

    public const string Score = "board_score";
    public const string Penalty = "board_penalty";
}

public static class ContextFormat
{
    public const string Json = "application/json";
    public const string FormData = "multipart/form-data";
}