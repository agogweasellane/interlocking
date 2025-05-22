using Interlocking.Base;
using Interlocking.Controllers;
using Interlocking.Framwork.Setting;
using Interlocking.Global;
using Interlocking.Global.Exception;
using Interlocking.Global.WrongException;
using Interlocking.Models.Daos;
using Interlocking.Models.ServiceLayer;
using Microsoft.AspNetCore.Builder;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Options;
using Microsoft.OpenApi.Models;
using MongoDB.Driver;
using StackExchange.Redis;
using Swashbuckle.AspNetCore.Filters;
using System;
using System.Reflection;


/// <summary>
/// .net9�� ������Ʈ �����ϴϱ�,
/// ASP .netCore 2.1�������� �ٸ��� StartUp.cs�� ������ �̰� �� ������ ���⵵�ѵ�
/// �������θ� ���� typescript�� App����?
/// </summary>

const string VERSION = "V. 0.8.2505";
var builder = WebApplication.CreateBuilder(args);
EnvironmentEnum envEnum = EnvironmentEnum.Win;
string appendTag = "";

string urls = urls = Environment.GetEnvironmentVariable("ASPNETCORE_WIN_URL");
if (!string.IsNullOrEmpty(urls))
{//������ ���� ���� ȯ��
}
else
{
    urls = Environment.GetEnvironmentVariable("ASPNETCORE_DOCKER_URL");
    envEnum = EnvironmentEnum.Docker;
    appendTag = "ForDocker";
}
builder.WebHost.UseUrls(urls);


//START. component��
builder.Services.AddDataToolSingleton();
//END. component��


builder.Services.AddOpenApi();


//START. Redis
builder.Services.AddSingleton<IConnectionMultiplexer>(provider =>
{
    string connectionString = builder.Configuration.GetConnectionString("Redis" + appendTag);
    return ConnectionMultiplexer.Connect(connectionString);
});
builder.Services.AddScoped<RedisService>(sp =>
    new RedisService(sp.GetRequiredService<IConnectionMultiplexer>(), sp.GetRequiredService<ILogger<RedisService>>()
));//END. Redis


//START. Maria DB
builder.Services.AddDbContext<MariaContext>(options => options.UseMySql(builder.Configuration.GetConnectionString("Maria"+ appendTag),
                                                                        Microsoft.EntityFrameworkCore.ServerVersion.AutoDetect(builder.Configuration.GetConnectionString("Maria" + appendTag))));
builder.Services.AddScoped<MariaService>();
//END. Maria DB


//START. ����DB
builder.Services.Configure<MongoSetting>(builder.Configuration.GetSection("MongoEcho"));
builder.Services.Configure<MongoSetting>(builder.Configuration.GetSection("MongoErrorLog"));

if(envEnum== EnvironmentEnum.Win)
{
    builder.Services.AddSingleton<IMongoClient>(sp => new MongoClient(sp.GetRequiredService<IOptions<MongoSetting>>().Value.ForLocal));
}
else
{
    builder.Services.AddSingleton<IMongoClient>(sp => new MongoClient(sp.GetRequiredService<IOptions<MongoSetting>>().Value.ForDocker));
}
builder.Services.AddScoped<IMongoDatabase>(sp => sp.GetRequiredService<IMongoClient>().GetDatabase(builder.Configuration["MongoEcho:DatabaseName"]));
builder.Services.AddScoped<IMongoDatabase>(sp => sp.GetRequiredService<IMongoClient>().GetDatabase(builder.Configuration["MongoErrorLog:DatabaseName"]));

var mongoEchoSection = builder.Configuration.GetSection("MongoEcho");
builder.Services.AddScoped<MongoMultiDocumentService>(sp =>
    new MongoMultiDocumentService(
        sp.GetRequiredService<IMongoDatabase>(), builder.Configuration.GetSection("MongoEcho")["CollectionName"],
        sp.GetRequiredService<DataTool>(),
        sp.GetRequiredService<ILogger<MongoMultiDocumentService>>()
));//END. ����DB

//START. ������
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(c =>
{// http://localhost:5001/swagger/index.html
    c.SwaggerDoc(VERSION, new OpenApiInfo { Title = "���ι���", Version = VERSION });
    c.EnableAnnotations();//API�� ���� ���
    c.ExampleFilters();    // Example ����

/*
EnableAnnotations�ɼ� ����,
SwaggerTag�� ���� �Ʒ����� ���� �߻�. ������ ���׷� �����Ǵµ� ����API�� SwaggerOperation�� �����۵�.
System.UriFormatException: Invalid URI: The format of the URI could not be determined.
    at System.Uri.CreateThis(String uri, Boolean dontEscape, UriKind uriKind, UriCreationOptions & creationOptions)
*/
});
builder.Services.AddSwaggerExamplesFromAssemblyOf<GlobalExceptionResponseExample>();
builder.Services.AddSwaggerExamplesFromAssemblyOf<EchoResponseExample>();
//END. ������



builder.Services.AddControllers(options =>
{
    options.Filters.Add<GlobalExceptionHandler>();
});
var app = builder.Build();


if (app.Environment.IsDevelopment())
{
    app.UseDeveloperExceptionPage(); //���� �� ����
    app.MapOpenApi();
    app.UseSwagger();
    app.UseSwaggerUI(options =>
    {
        options.SwaggerEndpoint("/swagger/v1/swagger.json", VERSION);
        options.RoutePrefix = string.Empty;//��Ʈ ��ο��� ����ϰ�
        options.ConfigObject.DisplayRequestDuration = true; // ���� Ȱ��ȭ
    });
}
else
{
    app.UseExceptionHandler("/error");
}

app.UseRouting(); // WARN. ����� �̵����
app.UseAuthorization();
app.MapControllers();
app.Run();
