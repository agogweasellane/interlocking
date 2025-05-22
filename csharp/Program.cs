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
/// .net9로 프로젝트 생성하니깐,
/// ASP .netCore 2.1버전과는 다르게 StartUp.cs가 없으니 이게 더 나은거 같기도한데
/// 육안으로만 보면 typescript의 App파일?
/// </summary>

const string VERSION = "V. 0.8.2505";
var builder = WebApplication.CreateBuilder(args);
EnvironmentEnum envEnum = EnvironmentEnum.Win;
string appendTag = "";

string urls = urls = Environment.GetEnvironmentVariable("ASPNETCORE_WIN_URL");
if (!string.IsNullOrEmpty(urls))
{//윈도우 로컬 개발 환경
}
else
{
    urls = Environment.GetEnvironmentVariable("ASPNETCORE_DOCKER_URL");
    envEnum = EnvironmentEnum.Docker;
    appendTag = "ForDocker";
}
builder.WebHost.UseUrls(urls);


//START. component류
builder.Services.AddDataToolSingleton();
//END. component류


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


//START. 몽고DB
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
));//END. 몽고DB

//START. 스웨거
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(c =>
{// http://localhost:5001/swagger/index.html
    c.SwaggerDoc(VERSION, new OpenApiInfo { Title = "내부문서", Version = VERSION });
    c.EnableAnnotations();//API별 설명 출력
    c.ExampleFilters();    // Example 필터

/*
EnableAnnotations옵션 사용시,
SwaggerTag로 인해 아래같은 오류 발생. 버전쪽 버그로 추정되는데 세부API별 SwaggerOperation은 정상작동.
System.UriFormatException: Invalid URI: The format of the URI could not be determined.
    at System.Uri.CreateThis(String uri, Boolean dontEscape, UriKind uriKind, UriCreationOptions & creationOptions)
*/
});
builder.Services.AddSwaggerExamplesFromAssemblyOf<GlobalExceptionResponseExample>();
builder.Services.AddSwaggerExamplesFromAssemblyOf<EchoResponseExample>();
//END. 스웨거



builder.Services.AddControllers(options =>
{
    options.Filters.Add<GlobalExceptionHandler>();
});
var app = builder.Build();


if (app.Environment.IsDevelopment())
{
    app.UseDeveloperExceptionPage(); //오류 상세 정보
    app.MapOpenApi();
    app.UseSwagger();
    app.UseSwaggerUI(options =>
    {
        options.SwaggerEndpoint("/swagger/v1/swagger.json", VERSION);
        options.RoutePrefix = string.Empty;//루트 경로에서 깔끔하게
        options.ConfigObject.DisplayRequestDuration = true; // 예시 활성화
    });
}
else
{
    app.UseExceptionHandler("/error");
}

app.UseRouting(); // WARN. 라우팅 미들웨어
app.UseAuthorization();
app.MapControllers();
app.Run();
