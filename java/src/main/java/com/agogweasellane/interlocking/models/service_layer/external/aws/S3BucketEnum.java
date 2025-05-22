package com.agogweasellane.interlocking.models.service_layer.external.aws;

import lombok.Getter;

@Getter
public enum S3BucketEnum
{
    //루트 버킷용
    root_private("interlocking-private", 0),
    root_public("interlocking-public", 1),
    
    
    //하위 폴더 경로.
    path_test("test/", 2),//test폴더가 idx=2에 무조건 있도록.
    //신규 하위폴더들은 여기서부터.
        
    none("", -1);

    private final String value;
    private final int permit;

    S3BucketEnum(String argValue, int argPermit)
    {
        value = argValue;
        permit = argPermit;
    }
}