using Interlocking.Models.Daos;
using Microsoft.EntityFrameworkCore;

namespace Interlocking.Framwork.Setting;

/// <summary>
/// MariaDB 연결
/// </summary>
public class MariaContext : DbContext
{
    public MariaContext(DbContextOptions<MariaContext> options) : base(options)
    {
    }

    public DbSet<TblEchoEntity> TblEchoEntities { get; set; }

    /// <summary>
    /// 엔티티마다.  [Table("tbl_xxx")]을 쓰니 OnModelCreating에 추가작업X
    /// </summary>
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {//MEMO. 엔티티마다. 어트리뷰트로 테이블명 지정하는걸로.
    }
}
