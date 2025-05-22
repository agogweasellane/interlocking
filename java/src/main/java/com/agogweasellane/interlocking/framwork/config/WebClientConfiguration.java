package com.agogweasellane.interlocking.framwork.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;


@Slf4j
@Configuration
public class WebClientConfiguration
{
	@Value("${custom.webclient.ExchangeStrategies.maxInMemorySize}")
	private int MEMORY_OVER_256KB;
	
	@Value("${custom.webclient.defaultHeader.key}")
	private String HEADER_KEY;
	@Value("${custom.webclient.defaultHeader.value}")
	private String HEADER_VALUE;
	
	@Bean
	public WebClient webClient() 
	{
		WebClient result = null;
		
		ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
	                                                                  .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(MEMORY_OVER_256KB))
	                                                                  .build();
		exchangeStrategies
	            .messageWriters().stream()
	            .filter(LoggingCodecSupport.class::isInstance)
	            .forEach(writer -> ((LoggingCodecSupport)writer).setEnableLoggingRequestDetails(true));

		result = WebClient.builder()
	                .clientConnector(new ReactorClientHttpConnector(
	                        HttpClient.create()
	                    )
	                )
	                .exchangeStrategies(exchangeStrategies)
	                .filter(ExchangeFilterFunction.ofRequestProcessor(
	                    clientRequest -> {
	                        log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
	                        clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
	                        return Mono.just(clientRequest);
	                    }
	                ))
	                .filter(ExchangeFilterFunction.ofResponseProcessor(
	                    clientResponse -> {
	                        clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
	                        return Mono.just(clientResponse);
	                    }
	                ))
	                .defaultHeader(HEADER_KEY, HEADER_VALUE)
	                .build();
		
		
		return result;
	}
}