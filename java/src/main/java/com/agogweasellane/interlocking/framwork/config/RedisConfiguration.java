package com.agogweasellane.interlocking.framwork.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@EnableCaching
@Configuration
public class RedisConfiguration
{
/*
https://redis.github.io/lettuce/

This version of Lettuce has been tested against the latest Redis source-build.

synchronous, asynchronous and reactive usage		<--Jedis는 동기식.
Redis Sentinel
Redis Cluster
SSL and Unix Domain Socket connections
Streaming API
... ... ...
Compatible with Java 8++ (implicit automatic module w/o descriptors)
----------------------------------------------------------------------------------------------
RedisTemplate vs RedisRepository

	https://hyperconnect.github.io/2022/12/12/fix-increasing-memory-usage.html
	https://jessyt.tistory.com/137
	https://davidy87.tistory.com/36		[Spring / Redis] RedisTemplate vs RedisRepository - 성능 비교 테스트
	->	저장 성능상 RedisRepository
*/
	
	@Value("${spring.data.redis.host}")
	private String REDIS_HOST;
	@Value("${spring.data.redis.port}")
	private int REDIS_PORT;
	@Value("${spring.data.redis.password}")
	private String REDIS_PASSWORD;
	
	@Value("${custom.redis.ping.key}")
	private String REDIS_PING;
	@Value("${custom.redis.ping.ttl.sec}")
	private long REDIS_PING_TTL;
	
	@Value("${custom.redis.default.ttl.sec}")
	private long DEFAULT_TTL;

	
	@Bean
	public LettuceConnectionFactory  redisCacheConnectionFactory()
	{
			RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
			redisConf.setHostName(REDIS_HOST);
			redisConf.setPort(REDIS_PORT);
			redisConf.setPassword(REDIS_PASSWORD);
	        return new LettuceConnectionFactory (redisConf);
	 }
	
//	 @Bean
//	 public RedisCacheConfiguration  cacheConfiguration()
//	 {
//	    return RedisCacheConfiguration.defaultCacheConfig()
//											      .entryTtl(Duration.ofSeconds(DEFAULT_TTL))
//											      .disableCachingNullValues()
//											      .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//	 }
//	
//	 @Bean
//	 public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer()
//	 {
//		 return (builder) -> builder.withCacheConfiguration(REDIS_PING, cacheConfiguration().entryTtl(Duration.ofSeconds(REDIS_PING_TTL)));
//	 }
}