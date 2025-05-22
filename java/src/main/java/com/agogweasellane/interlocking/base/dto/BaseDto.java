package com.agogweasellane.interlocking.base.dto;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;


@SuppressWarnings("rawtypes")
public class BaseDto<T extends BaseDto>
{
/*
	TO-DO: 제네릭 클래스 및 타입 파라미터에 대한 처리 보충 필요 [2023-03-18]
*/
	
	@JsonIgnore
	protected static final ObjectMapper pObjectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL)
																								.activateDefaultTyping(LaissezFaireSubTypeValidator.instance , DefaultTyping.NON_FINAL, As.PROPERTY);

	public T doDeserialize(byte[] argBytes)
	{
		T result = null;
		
		
		try
		{
			result = pObjectMapper.readValue(argBytes, new TypeReference<T>() {});
		} catch (StreamReadException e){
			e.printStackTrace();
		} catch (DatabindException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		
		
		return result;
	}
	
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
			e.printStackTrace();
		}
		
	    return result;
	}
}