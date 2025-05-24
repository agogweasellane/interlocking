using Amazon.S3.Model;

namespace Interlocking.Models.ServiceLayer;

public interface IAwsS3Service
{
    Task<List<S3Object>> ListObjectsAsync(RootInS3 root);
    Task<bool> UploadFileAsync(RootInS3 root, PathInS3 key, IFormFile[] file);
    Task<GetObjectResponse> DownloadObjectAsync(RootInS3 root, PathInS3 key);
    Task<bool> DeleteObjectAsync(RootInS3 root, PathInS3 key);
    Task<string> GetPreSignedURLAsync(RootInS3 root, PathInS3 key, int expirationInMinutes = 60);
    Task<byte[]> DownloadObjectAsByteArrayAsync(RootInS3 root, PathInS3 key);
    Task<Stream?> DownloadObjectAsStreamAsync(RootInS3 root, PathInS3 key);
}