using Interlocking.Global.Exception;
using Microsoft.AspNetCore.Mvc;
using Swashbuckle.AspNetCore.Annotations;

namespace Interlocking.Controllers;

/// <summary>
/// http://localhost:5001/apis/simple
/// ASP����ھ�� �����ϴ� ���� ������ API�� �⺻���� ����Ȯ�ο�.
/// [SwaggerTag(...)]�� EnableAnnotations�ɼ��ϰ� �浹��.
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
    [SwaggerOperation(Summary = "HttpGet Ȯ��", Description = "�⺻���� HttpGet Ȯ�ο� API")]
    public IEnumerable<WeatherForecast> Get()
    {
        return CommonResult();
    }

    [HttpPost]
    [SwaggerOperation(Summary = "HttpPost Ȯ��", Description = "�⺻���� HttpPost Ȯ�ο� API")]
    public IEnumerable<WeatherForecast> Post()
    {
        return CommonResult();
    }

    [HttpPut]
    [SwaggerOperation(Summary = "HttpPut Ȯ��", Description = "�⺻���� HttpPut Ȯ�ο� API")]
    public IEnumerable<WeatherForecast> Put()
    {
        return CommonResult();
    }

    [HttpDelete]
    [SwaggerOperation(Summary = "HttpDelete Ȯ��", Description = "�⺻���� HttpDelete Ȯ�ο� API")]
    public IEnumerable<WeatherForecast> Delete()
    {
        return CommonResult();
    }

    [HttpPatch]
    [SwaggerOperation(Summary = "HttpPatch Ȯ��", Description = "�⺻���� HttpPatch Ȯ�ο� API")]
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
