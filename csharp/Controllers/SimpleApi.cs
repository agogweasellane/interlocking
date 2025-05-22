using Interlocking.Global.Exception;
using Microsoft.AspNetCore.Mvc;
using Swashbuckle.AspNetCore.Annotations;

namespace Interlocking.Controllers;

/// <summary>
/// http://localhost:5001/apis/simple
/// ASP닷넷코어에서 제공하는 가장 간단한 API로 기본적인 구동확인용.
/// [SwaggerTag(...)]이 EnableAnnotations옵션하고 충돌남.
/// </summary>
[ApiController]
[Route("apis/simple")]
[ProducesResponseType(StatusCodes.Status200OK)]
[ProducesResponseType(StatusCodes.Status500InternalServerError)]
public class SimpleController : ControllerBase
{
    private static readonly string[] Summaries = new[]
    {
        "Freezing", "Bracing", "Chilly", "Cool", "Mild", "Warm", "Balmy", "Hot", "Sweltering", "Scorching"
    };

    private readonly ILogger<SimpleController> _logger;

    public SimpleController(ILogger<SimpleController> logger)
    {
        _logger = logger;
    }

    [HttpGet]
    [SwaggerOperation(Summary = "HttpGet 확인", Description = "기본적인 HttpGet 확인용 API")]
    public IEnumerable<WeatherForecast> Get()
    {
        return CommonResult();
    }

    [HttpPost]
    [SwaggerOperation(Summary = "HttpPost 확인", Description = "기본적인 HttpPost 확인용 API")]
    public IEnumerable<WeatherForecast> Post()
    {
        return CommonResult();
    }

    [HttpPut]
    [SwaggerOperation(Summary = "HttpPut 확인", Description = "기본적인 HttpPut 확인용 API")]
    public IEnumerable<WeatherForecast> Put()
    {
        return CommonResult();
    }

    [HttpDelete]
    [SwaggerOperation(Summary = "HttpDelete 확인", Description = "기본적인 HttpDelete 확인용 API")]
    public IEnumerable<WeatherForecast> Delete()
    {
        return CommonResult();
    }

    [HttpPatch]
    [SwaggerOperation(Summary = "HttpPatch 확인", Description = "기본적인 HttpPatch 확인용 API")]
    public IEnumerable<WeatherForecast> Patch()
    {
        return CommonResult();
    }

    private IEnumerable<WeatherForecast> CommonResult()
    {
        return Enumerable.Range(1, 5).Select(index => new WeatherForecast
        {
            Date = DateOnly.FromDateTime(DateTime.Now.AddDays(index)),
            TemperatureC = Random.Shared.Next(-20, 55),
            Summary = Summaries[Random.Shared.Next(Summaries.Length)]
        }).ToArray();
    }
}

public class WeatherForecast
{
    public DateOnly Date { get; set; }

    public int TemperatureC { get; set; }

    public int TemperatureF => 32 + (int)(TemperatureC / 0.5556);

    public string? Summary { get; set; }
}
