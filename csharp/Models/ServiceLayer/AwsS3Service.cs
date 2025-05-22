using Amazon;
using Amazon.S3;
using Amazon.S3.Model;
using Amazon.S3.Util;

using Interlocking.Base;

namespace Interlocking.Models.ServiceLayer;

public class AwsS3Service : IS3Service
{
    private readonly IAmazonS3 _s3Client;
    private readonly ILogger<S3Service> _logger;
    private readonly string _awsRegion;

    public S3Service(IAmazonS3 s3Client, ILogger<S3Service> logger, IConfiguration configuration)
    {
        _s3Client = s3Client;
        _logger = logger;
        _awsRegion = configuration.GetSection("AWS")["Region"]; // 설정에서 리전 정보 가져오기
    }
    public override bool IsAvailable()
    {
        throw new NotImplementedException();
    }

    public override Task<bool> IsAvailableAsync()
    {
        throw new NotImplementedException();
    }
}
