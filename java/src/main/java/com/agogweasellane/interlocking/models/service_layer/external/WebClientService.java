package com.agogweasellane.interlocking.models.service_layer.external;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.agogweasellane.interlocking.base.BaseDto;
import com.agogweasellane.interlocking.base.service.BaseService;
import com.agogweasellane.interlocking.models.service_layer.ExternalResponseEntity;

import reactor.core.publisher.Mono;


@Service
public class WebClientService extends BaseService
{
	//private final RestTemplate restTemplate;
	private final WebClient webClient;
	 
	 
	@Autowired
	public WebClientService(WebClient webClient)
	{//CASE. 생성자 주입
		this.webClient = webClient;
	}
	
	
	@Override
	public void doStartWithBooting()
	{
		
	}
	
	private ExternalResponseEntity processResponse(Mono<ResponseEntity<Resource>> arg)
	{
		ExternalResponseEntity result = null;
		
	     try
		{
	     	ResponseEntity<Resource> resource = arg.block();
			result = ExternalResponseEntity.builder().statusCode( resource.getStatusCode() )
															.responseBodyBytes( resource.getBody().getContentAsByteArray() )
															.build();
		} catch (WebClientResponseException e){
			result = ExternalResponseEntity.builder().statusCode( e.getStatusCode()  )
															.errorMessage( e.getMessage() )
															.build();
		}catch (Throwable e){
			result = ExternalResponseEntity.builder().statusCode( HttpStatus.INTERNAL_SERVER_ERROR )
															.errorMessage( e.getMessage() )
															.build();
		} 
	     
		return result;
	}

	public ExternalResponseEntity get(String argUrl)
	{
		ExternalResponseEntity result = null;
	     Mono<ResponseEntity<Resource>> tempResource = webClient.get().uri(argUrl)
															                .accept(MediaType.APPLICATION_JSON)
															                .retrieve().toEntity( Resource.class );
	     result = processResponse(tempResource);
		tempResource = null;
		
		return result;
	}
	
	public ExternalResponseEntity post(String argUrl, String argJson)
	{
		ExternalResponseEntity result = null;
	     Mono<ResponseEntity<Resource>> tempResource = webClient.post().uri(argUrl)
												     					 .bodyValue( argJson )
															                .accept(MediaType.APPLICATION_JSON)
															                .retrieve().toEntity( Resource.class );
		result = processResponse(tempResource);
		tempResource = null;
	     
		return result;
	}
	
	public ExternalResponseEntity put(String argUrl, String argJson)
	{
		ExternalResponseEntity result = null;
	     Mono<ResponseEntity<Resource>> tempResource = webClient.put().uri(argUrl)
												     					 .bodyValue( argJson )
															                .accept(MediaType.APPLICATION_JSON)
															                .retrieve().toEntity( Resource.class );
		result = processResponse(tempResource);
		tempResource = null;
		
		return result;
	}
	
	public ExternalResponseEntity delete(String argUrl)
	{
		ExternalResponseEntity result = null;
	     Mono<ResponseEntity<Resource>> tempResource = webClient.delete().uri(argUrl)
															                .accept(MediaType.APPLICATION_JSON)
															                .retrieve().toEntity( Resource.class );
		result = processResponse(tempResource);
		tempResource = null;
		
		return result;
	}


	@Override
	public boolean insert(boolean isOverWrite, BaseDto... argDto) {
		return false;
	}


	@Override
	public boolean update(BaseDto... argDto) {
		return false;
	}


	@Override
	public boolean update(String argQuery) {
		return false;
	}


	@Override
	public BaseDto findItem(BaseDto argDto) {
		return null;
	}


	@Override
	public List findItmes(BaseDto argDto) {
		return null;
	}


	@Override
	public long count() {
		return 0;
	}


	@Override
	public boolean delete(BaseDto... argDto) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'delete'");
	}
}