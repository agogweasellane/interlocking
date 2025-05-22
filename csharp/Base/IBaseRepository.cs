namespace Interlocking.Base;

public interface IBaseMongoRepository<T> where T : IMongoMultipleDocument
{
    Task<T> GetByIdAsync(string id);
    Task<T> GetByIdAsync(long id);
    Task CreateAsync(T document);
    Task UpdateAsync(string id, T updatedDocument);
    Task UpdateAsync(long id, T updatedDocument);
    Task RemoveAsync(string id);
    Task RemoveAsync(long id);
}

public interface IBaseSqlRepository<T> where T : BaseSqlEntity
{
    Task<T> GetByIdAsync(string id);
    Task<T> GetByIdAsync(long id);
    Task CreateAsync(T tuple);
    Task UpdateAsync(string id, T entity);
    Task UpdateAsync(long id, T entity);
    Task RemoveAsync(string id);
    Task RemoveAsync(long id);
}