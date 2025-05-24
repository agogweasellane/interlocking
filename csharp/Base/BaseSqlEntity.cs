using System.ComponentModel.DataAnnotations.Schema;

namespace Interlocking.Base;

public abstract class BaseSqlEntity
{
}

/// <summary>
/// primaryKey가 string인 Entity 추상클래스
/// </summary>
public abstract class BaseSqlSkeyEntity : BaseSqlEntity
{
    [Column("id")]
    public string Id { get; set; }
}

/// <summary>
/// primaryKey가 long인 Entity 추상클래스
/// </summary>
public abstract class BaseSqlNkeyEntity : BaseSqlEntity
{
    [Column("id")]
    public long Id { get; set; }
}
