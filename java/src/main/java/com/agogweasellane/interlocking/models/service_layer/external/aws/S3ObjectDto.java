package com.agogweasellane.interlocking.models.service_layer.external.aws;

import java.io.File;

import org.springframework.http.HttpStatus;

import com.agogweasellane.interlocking.base.BaseDto;
import com.agogweasellane.interlocking.controllers.FileDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class S3ObjectDto extends BaseDto
{
    @Builder.Default
    private S3BucketEnum root = S3BucketEnum.root_private;
    
    private String path;
    private FileDto file;
}
