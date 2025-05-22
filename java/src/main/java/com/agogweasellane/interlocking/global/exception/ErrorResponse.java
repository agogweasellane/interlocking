package com.agogweasellane.interlocking.global.exception;

import com.agogweasellane.interlocking.base.controller.BaseResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor//defaultConstructor
@Getter
@Setter
public class ErrorResponse extends BaseResponse<ErrorResponse>
{
}