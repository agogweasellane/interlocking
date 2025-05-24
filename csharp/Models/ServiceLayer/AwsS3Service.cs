using Amazon;
using Amazon.Runtime;
using Amazon.S3;
using Amazon.S3.Model;
using Amazon.S3.Util;

using Interlocking.Base;
using Interlocking.Global.WrongException;
using Microsoft.Extensions.Logging;
using System.Net;
using System.Reflection.Emit;

namespace Interlocking.Models.ServiceLayer;

/// <summary>
/// S3에 대한 로직 처리용 서비스 클래스
/// </summary>
public class AwsS3Service : BaseService, Interlocking.Models.ServiceLayer.IAwsS3Service
{
    private readonly IAmazonS3 _s3Client;
    private readonly IConfiguration _configuration;

    public AwsS3Service(IAmazonS3 s3Service, IConfiguration configuration,
                        ILogger<BaseService> logger) : base(logger)
    {
        _s3Client = s3Service;
        _configuration = configuration;
    }

    public Task<bool> DeleteObjectAsync(RootInS3 root, PathInS3 key)
    {
        throw new NotImplementedException();
    }

    public Task<byte[]> DownloadObjectAsByteArrayAsync(RootInS3 root, PathInS3 key)
    {
        throw new NotImplementedException();
    }

    public Task<Stream?> DownloadObjectAsStreamAsync(RootInS3 root, PathInS3 key)
    {
        throw new NotImplementedException();
    }

    public Task<GetObjectResponse> DownloadObjectAsync(RootInS3 root, PathInS3 key)
    {
        throw new NotImplementedException();
    }

    public Task<string> GetPreSignedURLAsync(RootInS3 root, PathInS3 key, int expirationInMinutes = 60)
    {
        throw new NotImplementedException();
    }

    /// <inheritdoc/>
    public override bool IsAvailable()
    {
        _logger.LogDebug("called.IsAvailable {0}", DateTime.Now);
        throw new NotImplementedException();
    }

    /// <inheritdoc/>
    public override async Task<bool> IsAvailableAsync()
    {
        _logger.LogDebug("called.IsAvailableAsync {0}", DateTime.Now);
        ListBucketsResponse listResponse = null;

        try
        {
            listResponse = await _s3Client.ListBucketsAsync();
        } catch (AmazonServiceException ex) {
            _logger.LogError(ex, "AWS IAM user OR access key CHECK!!!");
            return false;
        }

        if(listResponse.HttpStatusCode != HttpStatusCode.OK)
        {
            return false;
        }
        _logger.LogDebug("Bucket(s) = {}", listResponse.Buckets.Count);
        if (listResponse.Buckets.Count < (int)RootInS3.Max)
        {
            _logger.LogError("Missing Bucket. current={}", listResponse.Buckets.Count);
            return false;
        }

        return true;
    }

    public Task<List<S3Object>> ListObjectsAsync(RootInS3 root)
    {
        throw new NotImplementedException();
    }

    /// <inheritdoc/>
    public async Task<bool> UploadFileAsync(RootInS3 root, PathInS3 key, IFormFile[] file)
    {
        PutObjectRequest putRequest = null;
        string rootName = ConvertS3Enum.ToString(root);
        string pathName = ConvertS3Enum.ToString(key);

        foreach (var tmpFile in file)
        {
            using (var stream = tmpFile.OpenReadStream())
            {
                putRequest = new()
                {
                    BucketName = rootName,
                    Key = pathName,
                    InputStream = stream,
                    ContentType = tmpFile.ContentType
                };

                try{
                    var putResponse = await _s3Client.PutObjectAsync(putRequest);
                } catch (AmazonS3Exception ex) {
                    _logger.LogError(ex, "upload fail={0}", tmpFile.FileName);
                    throw new WrongServiceException();
                }
            }
        }

        return true;
    }
}

/// <summary>
/// S3의 루프 버킷(enum)
/// </summary>
public enum RootInS3
{
    PrivateInterlocking,
    PublicInterlocking,
    Max
}
/// <summary>
/// S3의 루프 버킷과 파일명 사이의 경로
/// </summary>
public enum PathInS3
{
    Test,
    Dev,
    Live,
    Max
}
/// <summary>
/// S3의 루프 버킷/경로에 대한 enum의 String변환
/// </summary>
public static class ConvertS3Enum
{
    private const string PrivateInterlocking = "interlocking-private";
    private const string PublicInterlocking = "interlocking-public";

    //하위 경로용
    private const string PathTest = "test";
    private const string PathDev = "dev";
    private const string PathLive = "live";

    private const string Max = "(총 갯수 확인용이니 다른데 사용X)";

    public static string ToString(RootInS3  rootEnum)
    {
        return rootEnum switch
        {
            RootInS3.PrivateInterlocking => PrivateInterlocking,
            RootInS3.PublicInterlocking => PublicInterlocking,
            RootInS3.Max => "알 수 없음"
        };
    }
    public static string ToString(PathInS3 rootEnum)
    {
        return rootEnum switch
        {
            PathInS3.Test => PathTest,
            PathInS3.Dev => PathDev,
            PathInS3.Live => PathLive,
            PathInS3.Max => "알 수 없음"
        };
    }
}