using Interlocking.Base;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Interlocking.Models.Daos;


/// <summary>
/// 관계형 테이블 통신/기본적인 CRUD 테스트에 사용.
/// </summary>
[Table("tbl_echo")]
public class TblEchoEntity : BaseSqlEntity
{
    [Key][Column("host_v4")][MaxLength(50)]
    public string HostV4 { get; set; }

    [Column("host_v6")][MaxLength(50)]
    public string HostV6 { get; set; }

    [Column("create_date")]
    public DateTime? CreateDate { get; set; } = null;

    [Column("edit_date")]
    public DateTime EditDate { get; set; } = DateTime.Now;

    [Column("create_timestamp")]
    public DateTime? CreateTimestamp { get; set; } = null;

    [Column("edit_timestamp")]
    public DateTime EditTimestamp { get; set; } = DateTime.Now;
}
