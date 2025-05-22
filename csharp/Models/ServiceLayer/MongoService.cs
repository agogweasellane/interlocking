using Interlocking.Base;
using Interlocking.Global;
using Interlocking.Models.ForEcho;
using Interlocking.Models.Settings;
using Microsoft.AspNetCore.Components.Forms;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using MongoDB.Driver;
using System;
using System.Reflection.Metadata;
using System.Xml;
using System.Xml.Linq;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace Interlocking.Models.ServiceLayer;


/// <summary>
/// Program.cs의 builder.Services.AddScoped<MongoMultiDocumentService>랑 생성자 입력순서 맞게 해도
/// 간혹 갱신이 더뎌서 에러가 나니 그럴때는 리부팅.
/// </summary>
public class MongoMultiDocumentService : BaseService, IBaseMongoRepository<EchoDocument>
{
    private readonly IMongoCollection<IMongoMultipleDocument> _collection;
    private readonly DataTool _dataTool;

    public MongoMultiDocumentService(IMongoDatabase database, string collectionName,
                                     DataTool dataTool,
                                     ILogger<MongoMultiDocumentService> logger) : base(logger)
    {
        _collection = database.GetCollection<IMongoMultipleDocument>(collectionName);
        _dataTool = dataTool;
    }


    //public async Task<T> GetByIdAsync<T>(string id) where T : class, IMongoMultipleDocument
    public async Task<EchoDocument> GetByIdAsync(string id)
    {
        EchoDocument ret = (EchoDocument)await _collection.Find(x => x.Id == id).FirstOrDefaultAsync();
        return ret;
    }

    public async Task<EchoDocument> GetByIdAsync(long id)
    {
        throw new NotImplementedException();
    }

    public async Task CreateAsync(EchoDocument document)
    {
        await _collection.InsertOneAsync(document);
    }

    public async Task UpdateAsync(string id, EchoDocument updatedDocument)
    {
        await _collection.ReplaceOneAsync(x => x.Id == id, updatedDocument);
    }

    public async Task RemoveAsync(string id)
    {
        await _collection.DeleteOneAsync(x => x.Id == id);
    }

    public override async Task<bool> IsAvailableAsync()
    {
        _logger.LogDebug("called.IsAvailableAsync {0}", DateTime.Now);


        //컬렉션관련 API를 직접 확인.
        EchoType2Document type2 = new ()
        {
            CreatedAt = DateTime.Now,
            Id = "test_" + _dataTool.GenerateString(10),
            Msg = _dataTool.GenerateString(64),
            ExpireAt = DateTime.Now.AddSeconds(ConstValue.TtlTermMin5)
        };

        try
        {
            await _collection.InsertOneAsync(type2);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, $"Error processing data: {type2.Id}");
            return false;
        }
        type2.Id = "test_" + _dataTool.GenerateString(10);
        type2.Msg = _dataTool.GenerateString(64);
        try
        {
            await _collection.InsertOneAsync(type2);
            await _collection.DeleteOneAsync(x => x.Id == type2.Id);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, $"Error processing data: {type2.Id}");
            return false;
        }


        //해당 서비스의 사용자 함수위주.
        EchoDocument type1 = new ()
        {
            Id = "test_" + _dataTool.GenerateString(8),
            Date = DateTime.Now,
            ExpireAt = DateTime.Now.AddSeconds(ConstValue.TtlTermMin5)
        };

        try
        {
            await CreateAsync(type1);
            await RemoveAsync(type1.Id);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, $"Error processing data: {type1.Id}");
            return false;
        }



        return true;
    }

    public override bool IsAvailable()
    {
        throw new NotImplementedException();
    }

    public Task UpdateAsync(long id, EchoDocument updatedDocument)
    {
        throw new NotImplementedException();
    }

    public Task RemoveAsync(long id)
    {
        throw new NotImplementedException();
    }
}
