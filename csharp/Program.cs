using Amazon.Extensions.NETCore.Setup;
using Amazon.S3;

using Interlocking.Base;
using Interlocking.Controllers;
using Interlocking.Framwork.Binder;
using Interlocking.Framwork.Setting;
using Interlocking.Global;
using Interlocking.Global.Exception;
using Interlocking.Models.ServiceLayer;
using Microsoft.AspNetCore.Http.Features;
using Microsoft.AspNetCore.HttpLogging;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging.Console;
using Microsoft.Extensions.Options;
using Microsoft.OpenApi.Models;
using MongoDB.Bson;
using MongoDB.Driver;
using StackExchange.Redis;
using Swashbuckle.AspNetCore.Filters;


/// <summary>
/// .net9�� ������Ʈ �����ϴϱ�,
/// ASP .netCore 2.1/3.x �������� �ٸ��� StartUp.cs�� ������ �̰� �� ������ ���⵵�ѵ�
/// �������θ� ���� typescript�� App����?
/// </summary>
var builder = WebApplication.CreateBuilder(args);
using var loggerFactory = LoggerFactory.Create(builder =>
{
    builder.AddSimpleConsole(i => i.ColorBehavior = LoggerColorBehavior.Disabled);
});
var logger = loggerFactory.CreateLogger<Program>();

EnvironmentEnum envEnum = EnvironmentEnum.Win;
string appendTag = "";
const string API_VERSION = "V. 0.8.2505";

string? urls = urls = Environment.GetEnvironmentVariable("ASPNETCORE_WIN_URL");
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


builder.Services.Configure<FormOptions>(options =>
{//multipartForm.÷������
    options.MultipartBodyLengthLimit = long.MaxValue; // ���� ����
    options.ValueLengthLimit = int.MaxValue;       // ���� �� �ʵ��� �ִ� ����
    options.MultipartHeadersLengthLimit = 8192;   // multipart ����� �ִ� ����
});


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
    c.SwaggerDoc("v1", new OpenApiInfo { Title = "���ι���", Version = API_VERSION });//���� ��ܿ� ǥ��Ǵ� ��.
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


//START. Ŭ����-AWS
var awsSettingJson = builder.Configuration.GetSection("AWS").Get<AwsSetting>();
logger.LogDebug("GetAWSOptions={0}", awsSettingJson.ToJson());
builder.Services.AddDefaultAWSOptions(new AWSOptions
{
    Region = Amazon.RegionEndpoint.GetBySystemName(awsSettingJson?.Region),
    Credentials = new Amazon.Runtime.BasicAWSCredentials(awsSettingJson?.AccessKey, awsSettingJson?.SecretKey)
});
builder.Services.AddAWSService<IAmazonS3>();
//builder.Services.AddAWSService<IAmazonS3A>(Configuration.GetAWSOptions("AWS1"));//�߰����� A ȣȯ�ǰ� �� ��.
//builder.Services.AddAWSService<IAmazonS3B>(Configuration.GetAWSOptions("AWS2"));//�߰����� B ȣȯ�ǰ� �� ��.
builder.Services.AddScoped<Interlocking.Models.ServiceLayer.IAwsS3Service, AwsS3Service>();
//END. Ŭ����-AWS


builder.Services.AddHttpLogging(o => {//�α�.
    o.LoggingFields = HttpLoggingFields.All & ~HttpLoggingFields.Duration;
});
builder.Services.AddControllers(options =>
{
    options.Filters.Add<GlobalExceptionHandler>();
    options.ModelBinderProviders.Insert(0, new MultiFormBinderProvider());
});
builder.Services.AddTransient<MultiFormReqBinder<BaseRequestPacket>>();


var app = builder.Build();


if (app.Environment.IsDevelopment())
{
    app.UseDeveloperExceptionPage(); //���� �� ����
    app.MapOpenApi();
    app.UseSwagger();
    app.UseSwaggerUI(options =>
    {
        options.SwaggerEndpoint("/swagger/v1/swagger.json", API_VERSION);
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
app.UseHttpLogging();//�α�.
app.MapControllers();
app.Run();
