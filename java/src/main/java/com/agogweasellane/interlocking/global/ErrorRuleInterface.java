package com.agogweasellane.interlocking.global;

import org.springframework.http.HttpStatus;

import com.agogweasellane.interlocking.global.exception.ErrorResponse;


public interface ErrorRuleInterface
{
	default ErrorResponse setErrorResponse(ErrorMessageEnum argEnum)
	{
		return setErrorResponse(argEnum.getStatus(), argEnum.getCode(), "");
	}
	default ErrorResponse setErrorResponse(HttpStatus argStatue, String argCode)
	{
		return setErrorResponse(argStatue, argCode, "");
	}
	default ErrorResponse setErrorResponse(HttpStatus argStatue, String argCode, String argTag)
	{
		ErrorResponse result = ErrorResponse.builder()
											.status( argStatue ).code(argCode)
											.tag(argTag)
											.timeStamp(-1)
											.message("")
											.build();
		
		return result;
	}
}