using Interlocking.Base;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;
using System.Xml.Linq;

namespace Interlocking.Models.ForEcho;


/// <summary>
/// (몽고DB)tbl_echo에 2종류 이상 도큐먼트 입력 테스트 [1/2]
/// </summary>
public class EchoDocument : IMongoMultipleDocument
{
    public string Id { get; set; }

    public string DocumentType { get; set; } = "Echo";

    [Required(ErrorMessage = "Required.Date")]
    public DateTime Date { get; set; }

    //public DateTimeOffset ExpireAt { get; set; }
    public DateTime ExpireAt { get; set; }
}

/// <summary>
/// (몽고DB)tbl_echo에 2종류 이상 도큐먼트 입력 테스트 [2/2]
/// </summary>
public class EchoType2Document : IMongoMultipleDocument
{
    public string Id { get; set; }

    [JsonPropertyName("documentType")]
    public string DocumentType { get; set; } = "EchoType2";

    [JsonPropertyName("msg")]
    [Required(ErrorMessage = "Required.Msg")]
    public string Msg { get; set; }

    public DateTime CreatedAt { get; set; }

    public DateTime? UpdatedAt { get; set; }

    public DateTime ExpireAt { get; set; }
}