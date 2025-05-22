using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System.ComponentModel.DataAnnotations;

namespace Interlocking.Base;

/// <summary>
/// _id를 사용할 도큐먼트 & 한 테이블에 여러종류의 도큐먼트 입력시 필수.
/// </summary>
public interface IMongoMultipleDocument
{
    [Required(ErrorMessage = "Required.Id")]
    [BsonRepresentation(BsonType.String)]
    [BsonElement("_id")]
    [BsonId]
    public string Id { get; set; }
    //public string? Id { get; set; }
}