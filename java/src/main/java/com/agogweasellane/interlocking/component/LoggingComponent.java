package com.agogweasellane.interlocking.component;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoggingComponent
{
	public void printException(Exception argE)
	{
		log.error( "\n LocalizedMessage. {}\n Message. {}", argE.getLocalizedMessage(), argE.getMessage() );
	}
}