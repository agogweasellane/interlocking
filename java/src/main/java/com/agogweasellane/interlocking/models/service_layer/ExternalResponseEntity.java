package com.agogweasellane.interlocking.models.service_layer;

import org.springframework.http.HttpStatusCode;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExternalResponseEntity
{
	private HttpStatusCode statusCode;
	private byte[] responseBodyBytes;
	private String errorMessage;
	
	
	@Builder
	public ExternalResponseEntity(HttpStatusCode statusCode, byte[] responseBodyBytes, String errorMessage)
	{
		this.statusCode = statusCode;
		this.responseBodyBytes = responseBodyBytes;
		this.errorMessage = errorMessage;
	}
	
	public boolean isError()
	{
		return this.statusCode.isError();
	}
}