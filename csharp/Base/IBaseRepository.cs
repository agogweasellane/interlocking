namespace Interlocking.Base;

/// <summary>
/// Interface for a MongoDB 레포지토리.
/// </summary>
/// <typeparam name="T">도큐먼트 클래스 implement IMongoMultipleDocument.</typeparam>
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

/// <summary>
/// Interface for a base SQL repository.
/// </summary>
/// <typeparam name="T">엔티티 클래스 implement BaseSqlEntity.</typeparam>
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