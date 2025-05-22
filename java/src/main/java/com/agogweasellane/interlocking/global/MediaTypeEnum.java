package com.agogweasellane.interlocking.global;

public enum MediaTypeEnum
{
    PNG("png", "image/png"),
    JPEG("jpeg", "image/jpeg"),
    JPG("jpg", "image/jpeg"),
    GIF("gif", "image/gif"),
    MP4("mp4", "video/mp4"),
    AVI("avi", "video/x-msvideo"),
    MOV("mov", "video/quicktime"),
    MP3("mp3", "audio/mpeg"),
    WAV("wav", "audio/wav");

    private final String extension;
    private final String mimeType;

    MediaTypeEnum(String extension, String mimeType)
    {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getExtension()
    {
        return extension;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public static boolean isMedia(String filename)
    {
        if (filename == null || !filename.contains(DefaultInterface.TOKEN_DOT)) {
            return false;
        }
        String fileExtension = filename.substring(filename.lastIndexOf(DefaultInterface.TOKEN_DOT) + 1).toLowerCase();
        for (MediaTypeEnum mediaType : MediaTypeEnum.values())
        {
            if (mediaType.getExtension().equals(fileExtension))
            {
                return true;
            }
        }
        return false;
    }
    public static String getContentType(String filename)
    {
        if (filename == null || !filename.contains(DefaultInterface.TOKEN_DOT)) {
            return null;
        }
        String fileExtension = filename.substring(filename.lastIndexOf(DefaultInterface.TOKEN_DOT) + 1).toLowerCase();
        for (MediaTypeEnum mediaType : MediaTypeEnum.values())
        {
            if (mediaType.getExtension().equals(fileExtension))
            {
                return mediaType.getMimeType();
            }
        }

        return null;
    }
}