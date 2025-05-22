using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace Interlocking.Models.ForEcho;

public class ErrorDocument
{//WARN. MultipleDocumentX
    [Required(ErrorMessage = "Required.Id")]
    [BsonId]
    [BsonRepresentation(BsonType.ObjectId)]
    public string Id { get; set; }

    [JsonPropertyName("msg")]
    [Required(ErrorMessage = "Required.Msg")]
    public string Msg { get; set; }

    public DateTime CreatedAt { get; set; }

    public DateTimeOffset ExpireAt { get; set; }
}