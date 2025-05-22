package com.agogweasellane.interlocking.controllers;

import com.agogweasellane.interlocking.base.BaseDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FileDto extends BaseDto
{
    private byte[] bytes;
    private String contentType;
    private String format;
    private String name;
    private String fullName;
}