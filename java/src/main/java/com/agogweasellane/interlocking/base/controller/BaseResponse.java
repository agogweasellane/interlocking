package com.agogweasellane.interlocking.base.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("rawtypes")
@Slf4j
@SuperBuilder
@NoArgsConstructor//defaultConstructor
@Setter
@Getter
public class BaseResponse <T extends BaseResponse>//extends BaseSeserialize<BaseResponse>
{
	@JsonIgnore
	protected static final ObjectMapper pObjectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);

	@Builder.Default
	protected HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
	
	protected String tag;
	protected String code;
	protected String message;

	@Builder.Default protected long timeStamp = -1;

	public T doDeserialize(byte[] argBytes)
	{
		T result = null;
		
		try
		{
			result = pObjectMapper.readValue(argBytes, new TypeReference<T>() {});
		} catch (StreamReadException e){
			log.error( e.getMessage() );
		} catch (DatabindException e){
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
/*
빌더패턴
	https://resilient-923.tistory.com/418
	https://mangkyu.tistory.com/163
	https://velog.io/@mihyun/Lombok-Builder-상속

MEMO.
	Lombok의 @Builder는 static 매소드여서 상속의 경우에 지원X -> @SuperBuilder를 부모/자식에게
*/