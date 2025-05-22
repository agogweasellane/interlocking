using Interlocking.Base;
using Interlocking.Global;
using Newtonsoft.Json;
using StackExchange.Redis;
using System;
using static MongoDB.Bson.Serialization.Serializers.SerializerHelper;

namespace Interlocking.Models.ServiceLayer;

public class RedisService : BaseService
{
    private readonly IConnectionMultiplexer _redis;
    private readonly IDatabase _db;

    public RedisService(IConnectionMultiplexer redis, ILogger<RedisService> logger) : base(logger)
    {
        _redis = redis;
        _db = _redis.GetDatabase();
    }


    public override bool IsAvailable()
    {
        throw new NotImplementedException();
    }

    public override async Task<bool> IsAvailableAsync()
    {
        try
        {
            bool tmp = false;

            await _db.SetAddAsync(RedisBoard.Echo, RedisBoard.Fake);

            tmp = await _db.SetContainsAsync(RedisBoard.Echo, RedisBoard.Fake);
            if(tmp==false) return false;

            await _db.SetRemoveAsync(RedisBoard.Echo, RedisBoard.Fake);

            tmp = await _db.SetContainsAsync(RedisBoard.Echo, RedisBoard.Fake);
            if (tmp == true) return false;

            await _db.SetAddAsync(RedisBoard.Echo, RedisBoard.Fake + "2");
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, $"Error processing RedisBoard: {RedisBoard.Echo}");
            return false;
        }


        return true;
    }
}