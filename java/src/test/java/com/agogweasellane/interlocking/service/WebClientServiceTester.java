package com.agogweasellane.interlocking.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import com.agogweasellane.interlocking.models.service_layer.ExternalResponseEntity;
import com.agogweasellane.interlocking.models.service_layer.external.WebClientService;

import lombok.extern.slf4j.Slf4j;


//@ExtendWith(MockitoExtension.class)//JUnit5
@Slf4j
public class WebClientServiceTester
{
	private WebClientService service;
	private String url = "https://www.google.com";

	@Test
	public void testClient()
     {
		WebClient client = WebClient.create();
		ResponseEntity<Resource> result = client.get().uri(url)
												                .accept(MediaType.APPLICATION_JSON)
												                .retrieve().toEntity( Resource.class ).block();
		assertThat( result ).isNotNull();
		assertThat( result.getStatusCode().value() ).isEqualTo( HttpStatus.OK.value() );
     }
	
	@Test
	public void testGet()
     {
		if(service==null)	{	service = new WebClientService( WebClient.create() );	}

		ExternalResponseEntity result = service.get(url);
		assertThat( result ).isNotNull();
		assertThat( result.getStatusCode().value() ).isEqualTo( HttpStatus.OK.value() );
		assertThat( result.isError() ).isEqualTo( false );
     }
	
	
	@Test
	public void testPost()
     {
		if(service==null)	{	service = new WebClientService( WebClient.create() );	}

		ExternalResponseEntity result = service.post(url, "{}");
		assertThat( result ).isNotNull();
		assertThat( result.getStatusCode().value() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
		assertThat( result.isError() ).isEqualTo( true );
     }
	
	
	@Test
	public void testPut()
     {
		if(service==null)	{	service = new WebClientService( WebClient.create() );	}

		ExternalResponseEntity result = service.put(url, "{}");
		assertThat( result ).isNotNull();
		assertThat( result.getStatusCode().value() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
		assertThat( result.isError() ).isEqualTo( true );
     }
	
	
	@Test
	public void testDelete()
     {
		if(service==null)	{	service = new WebClientService( WebClient.create() );	}

		ExternalResponseEntity result = service.delete(url);
		assertThat( result ).isNotNull();
		assertThat( result.getStatusCode().value() ).isEqualTo( HttpStatus.METHOD_NOT_ALLOWED.value() );
		assertThat( result.isError() ).isEqualTo( true );
     }
}
/*
https://gogo-jjm.tistory.com/9


	https://howtodoinjava.com/spring-boot2/resttemplate/resttemplate-httpclient-java-config/
	> 관련 클래스 전반. 어노테이션등 일부 옵션등이 좀 다른 부분 있음.
	
	https://howtodoinjava.com/spring-boot2/resttemplate/spring-restful-client-resttemplate-example/#get-example
	https://howtodoinjava.com/spring-boot2/resttemplate/spring-restful-client-resttemplate-example/#post-example
	
	https://www.baeldung.com/rest-template
	
	https://medium.com/@odysseymoon/spring-webclient-사용법-5f92d295edc0
	https://www.baeldung.com/spring-mocking-webclient
	https://wave1994.tistory.com/179
	
	https://www.baeldung.com/spring-5-webclient
	https://gngsn.tistory.com/154
	
	
	https://eenn.tistory.com/entry/spring5-webclient-post-json-sample
	https://docs.spring.io/spring-framework/docs/5.0.5.RELEASE/javadoc-api/org/springframework/web/reactive/function/client/WebClient.Builder.html#defaultHeaders-java.util.function.Consumer-
	https://stackoverflow.com/questions/45301220/how-to-mock-spring-webflux-webclient		WebClientService( WebClient.create() );
*/