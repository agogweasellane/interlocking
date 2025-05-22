package com.agogweasellane.interlocking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServletComponentScan // for XxxxFilter.
@EnableJpaAuditing	//@CreatedDate, @LastModifiedDate
@SpringBootApplication
public class InterlockingApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(InterlockingApplication.class, args);

		log.debug("SpringApplication_START");
		log.info("SpringApplication_START");
		log.warn("SpringApplication_START");
		log.error("SpringApplication_START");
	}
}