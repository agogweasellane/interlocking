package com.agogweasellane.interlocking.base.controller;

import java.io.IOException;
import java.io.InputStream;

import com.agogweasellane.interlocking.base.ApiMethodEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;


@SuppressWarnings("rawtypes")
@Slf4j
@SuperBuilder
@NoArgsConstructor//defaultConstructor
@Setter(value = AccessLevel.NONE)
@Getter
public class BaseRequest <T extends BaseRequest>//extends BaseSeserialize<BaseRequest>
{
	@JsonIgnore
	protected static final ObjectMapper pObjectMapper = new ObjectMapper();
	//new ObjectMapper().setSerializationInclusion(Include.NON_NULL);

    protected ApiMethodEnum method;
    //protected String method;

    //public T doDeserialize(Class<T> argClass, InputStream arg)
    public T doDeserialize(InputStream arg)
    {
		T result = null;

		try
		{
			pObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			result = doDeserialize( arg.readAllBytes() );
		} catch (OutOfMemoryError e){
			log.error( e.getMessage() );
		} catch (IOException e){
			log.error( e.getMessage() );
		}

		return result;
	}
    
    //public T doDeserialize(Class<T> argClass, byte[] argBytes)
    public T doDeserialize( byte[] argBytes)
    {
		T result = null;

		try
		{
			pObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			//result = pObjectMapper.readValue(argBytes, new TypeReference<BaseRequest<T>>() {});
			result = (T) pObjectMapper.readValue(argBytes, this.getClass());
		} catch (StreamReadException e){
			log.error( e.getMessage() );
		} catch (DatabindException e){
			log.error( e.getMessage() );
		} catch (ClassCastException e){
			log.error( e.getMessage() );
		} catch (IOException e){
			log.error( e.getMessage() );
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
			log.error( e.getMessage() );
		}
		
	    return result;
	}
}