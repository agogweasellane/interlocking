package com.agogweasellane.interlocking.controllers.api.simple.dto;

import com.agogweasellane.interlocking.base.controller.BaseResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@NoArgsConstructor//defaultConstructor
public class SimpleResponse extends BaseResponse<SimpleResponse>
{
    private String testVar;
}