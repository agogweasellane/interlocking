using Interlocking.Base;
using Interlocking.Framwork.Setting;
using Interlocking.Global;
using Interlocking.Models.Daos;
using Interlocking.Models.ForEcho;
using Microsoft.EntityFrameworkCore;
using System;

namespace Interlocking.Models.ServiceLayer;


/// <summary>
/// _id기반으로 CRUD이니 해당 서비스에서는 id:long기반 함수는 throw new NotImplementedException()로만.
/// </summary>
public class MariaService : BaseService, IBaseSqlRepository<TblEchoEntity>
{
    private readonly MariaContext _context;
    private readonly DataTool _dataTool;

    public MariaService(MariaContext context,
                         DataTool dataTool,
                        ILogger<BaseService> logger) : base(logger)
    {
        _context = context;
        _dataTool = dataTool;
    }

    public override bool IsAvailable()
    {
        throw new NotImplementedException();
    }

    public override async Task<bool> IsAvailableAsync()
    {
        TblEchoEntity echoEntity = new()
        {
            HostV4 = _dataTool.GetIPv4Address(),
            HostV6 = "::1",
        };
        //echoEntity.EditDate = DateTime.Now;
        echoEntity.CreateDate = echoEntity.EditDate;
        //echoEntity.EditTimestamp = DateTimeOffset.UtcNow.ToOffset(TimeSpan.FromHours(ConstValue.UTC_OFFSET)).DateTime;
        echoEntity.CreateTimestamp = echoEntity.EditTimestamp;

        try
        {
            TblEchoEntity? entityFromTbl = null;
            entityFromTbl = await _context.Set<TblEchoEntity>().FindAsync(echoEntity.HostV4);
            bool isUpdate = true;

            if (entityFromTbl == null)
            {//CASE. 최초 입력
                await _context.Set<TblEchoEntity>().AddAsync(echoEntity);
                await _context.SaveChangesAsync();
                isUpdate = false;
            }
            else
            {   //WARN. EF Core의 변경 추적 방식과 데이터베이스 스키마를 고려할 때 잠재적인 위험
                //echoEntity.CreateDate = null;
                //echoEntity.CreateTimestamp = null;

                entityFromTbl.EditDate = DateTime.Now;
                entityFromTbl.EditTimestamp = DateTimeOffset.UtcNow.ToOffset(TimeSpan.FromHours(ConstValue.UTC_OFFSET)).DateTime;

                _context.Entry(entityFromTbl).State = EntityState.Modified;
                await _context.SaveChangesAsync();
            }

            entityFromTbl = await _context.Set<TblEchoEntity>().FindAsync(echoEntity.HostV4);
            if (entityFromTbl == null)
            {
                _logger.LogError("MARIADB. AddAsync not work");
                return false;
            }
            if(isUpdate==true)
            {
                if(entityFromTbl.EditDate == entityFromTbl.CreateDate.Value)
                {
                    _logger.LogError("MARIADB. EditDate wrong");
                    return false;
                }
            }
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, $"Error creating YourSqlEntity");
            return false;
        }


        return true;
    }



    public async Task<TblEchoEntity> GetByIdAsync(string id)
    {
        try
        {
            return await _context.Set<TblEchoEntity>().FindAsync(id);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, $"Error getting YourSqlEntity with string ID {id}");
            return null;
        }
    }

    public async Task<TblEchoEntity> GetByIdAsync(long id)
    {
        throw new NotImplementedException();
    }

    public async Task CreateAsync(TblEchoEntity tuple)
    {
        try
        {
            await _context.Set<TblEchoEntity>().AddAsync(tuple);
            await _context.SaveChangesAsync();
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, $"Error creating YourSqlEntity");
            throw;
        }
    }

    public async Task UpdateAsync(string id, TblEchoEntity entity)
    {
        try
        {
            var existingEntity = await _context.Set<TblEchoEntity>().FindAsync(id);
            if (existingEntity != null)
            {
                _context.Entry(existingEntity).CurrentValues.SetValues(entity);
                await _context.SaveChangesAsync();
            }
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, $"Error updating YourSqlEntity with string ID {id}");
            throw;
        }
    }

    public async Task UpdateAsync(long id, TblEchoEntity entity)
    {
        throw new NotImplementedException();
    }

    public async Task RemoveAsync(string id)
    {
        try
        {
            var entityToRemove = await _context.Set<TblEchoEntity>().FindAsync(id);
            if (entityToRemove != null)
            {
                _context.Set<TblEchoEntity>().Remove(entityToRemove);
                await _context.SaveChangesAsync();
            }
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, $"Error removing YourSqlEntity with string ID {id}");
            throw;
        }
    }

    public async Task RemoveAsync(long id)
    {
        throw new NotImplementedException();
    }
}