package com.agogweasellane.interlocking.controllers.api.echo.dto;

import com.agogweasellane.interlocking.base.BaseDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EchoDto extends BaseDto
{
	private String host_v4;
}