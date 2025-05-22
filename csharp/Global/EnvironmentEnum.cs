namespace Interlocking.Global;


/// <summary>
/// 지금은 배포환경으로만 나눴지만, 조만간 빌드타입별로도 경우의 수 늘어날 예정.
/// </summary>
public enum EnvironmentEnum
{
    Win,

    Docker
}