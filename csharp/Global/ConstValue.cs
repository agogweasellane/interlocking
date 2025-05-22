namespace Interlocking.Global;

public static class ConstValue
{
    public const int TtlTermWeek1 = 7*24*3600;
    public const int TtlTermDay1 = 1 * 24 * 3600;
    public const int TtlTermMin5 = 5 * 60;

    public const int UTC_OFFSET = 9;
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
}
