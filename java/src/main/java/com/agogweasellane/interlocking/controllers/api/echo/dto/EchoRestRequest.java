package com.agogweasellane.interlocking.controllers.api.echo.dto;

import java.io.File;

import com.agogweasellane.interlocking.base.controller.BaseRequest;
import com.agogweasellane.interlocking.controllers.FileDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EchoRestRequest extends BaseRequest<EchoRestRequest>
{
	@JsonProperty("test") private String test;

	//@JsonProperty("files") private MultipartFile[] files; 
	@JsonProperty("files") private FileDto[] files; 
}