using System.Net.NetworkInformation;
using System.Security.Cryptography;

namespace Interlocking.Global;

public static class DataToolExtensions
{
    public static IServiceCollection AddDataToolSingleton(this IServiceCollection services)
    {
        services.AddSingleton<DataTool>();
        return services;
    }

    /// <summary>
    /// CS1106 Error 때문에 static
    /// </summary>
    /// <param name="file">C#의 IFormFile</param>
    /// <returns>범용성을 위해 byte[]</returns>
    public static async Task<byte[]?> ToByteArrayAsync(this IFormFile file)
    {
        if (file == null || file.Length == 0)
        {
            return null;
        }

        using (var memoryStream = new MemoryStream())
        {
            await file.CopyToAsync(memoryStream);
            return memoryStream.ToArray();
        }
    }
}

public class DataTool
{
    private const string Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public string GenerateString(int length)
    {
        byte[] randomBytes = new byte[length];
        using (var rng = RandomNumberGenerator.Create())
        {
            rng.GetBytes(randomBytes);
        }
        return new string(randomBytes.Select(b => Chars[b % Chars.Length]).ToArray());
    }

    public string GetIPv4Address()
    {
        var networkInterfaces = NetworkInterface.GetAllNetworkInterfaces();
        foreach (var networkInterface in networkInterfaces)
        {
            if (networkInterface.OperationalStatus == OperationalStatus.Up &&
                networkInterface.NetworkInterfaceType != NetworkInterfaceType.Loopback)
            {
                var ipProperties = networkInterface.GetIPProperties();
                if (ipProperties?.UnicastAddresses != null)
                {
                    foreach (var ipAddressInfo in ipProperties.UnicastAddresses)
                    {
                        if (ipAddressInfo.Address.AddressFamily == System.Net.Sockets.AddressFamily.InterNetwork)
                        {
                            return ipAddressInfo.Address.ToString();
                        }
                    }
                }
            }
        }
        return "127.0.0.1";
    }
}