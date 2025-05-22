using System.ComponentModel.DataAnnotations;

namespace Interlocking.Framwork.Setting;

/// <summary>
/// appsettings.json에 등록한 몽고DB 접속정보 JSON 참조.
/// </summary>
public class MongoSetting
{
    [Required] public string ForDocker { get; set; } = null!;

    [Required] public string ForLocal { get; set; } = null!;

    [Required] public string DatabaseName { get; set; } = null!;

    [Required] public string CollectionName { get; set; } = null!;
}
