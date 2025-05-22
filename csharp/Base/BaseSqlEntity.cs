using System.ComponentModel.DataAnnotations.Schema;

namespace Interlocking.Base;

public abstract class BaseSqlEntity
{
}
public abstract class BaseSqlSkeyEntity : BaseSqlEntity
{
    [Column("id")]
    public long Id { get; set; }
}

public abstract class BaseSqlNkeyEntity : BaseSqlEntity
{
    [Column("id")]
    public long Id { get; set; }
}
