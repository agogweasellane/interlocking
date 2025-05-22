package com.agogweasellane.interlocking.controllers.api.simple.dto;

import com.agogweasellane.interlocking.base.ApiMethodEnum;
import com.agogweasellane.interlocking.base.controller.BaseRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
public class SimpleRequest
{
	@JsonIgnore
	protected static final ObjectMapper pObjectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
	
	@NotNull
	private ApiMethodEnum method;
	
	public String doSeserialize()
	{
		String result = null;
		
		if(pObjectMapper!=null)
		{
			pObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		}

		try
		{
			result = pObjectMapper.writeValueAsString(this);
		}  catch (JsonProcessingException e){
			log.error( e.getMessage() );
		}
		
	    return result;
	}
}