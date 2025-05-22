package com.agogweasellane.interlocking.controllers.api.simple;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.agogweasellane.interlocking.controllers.api.simple.dto.SimpleRequest;
import com.agogweasellane.interlocking.controllers.api.simple.dto.SimpleResponse;
import com.agogweasellane.interlocking.controllers.param.RestrictedParam;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = RestrictedParam.LOCAL_SIMPLE)
@Tag(name="SimpleController",
	 description = "기본 동작 및 구조 견본. 윈도우 로컬 환경에서만 구동하게 제한있음. / BaseRestController 상속X")
public class SimpleController// <I extends SimpleRequest, O extends SimpleResponse>
{
	private final SpringTemplateEngine mTemplateEngine;

	@Autowired
	public SimpleController(SpringTemplateEngine templateEngine)
	{//CASE. 생성자 주입
		this.mTemplateEngine = templateEngine;
	}

	private String currentTime()
	{
		String result = null;
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-DD HH:mm:ss");
		formatter.withLocale( Locale.KOREA );
		
		return now.format(formatter);
	}
	
	
	@RequestMapping(method=RequestMethod.GET)
	public String doGet()
	{
		log.debug( "doGet called" );

		return "this is simple GET api" + "\n memo   "+ currentTime();
	}
	
	@RequestMapping(method=RequestMethod.GET, path = "/view", produces = MediaType.TEXT_HTML_VALUE)
	public String doGetView()
	{
		Context result = new Context();
		
		result.setVariable("currentTime", currentTime());
		
		return mTemplateEngine.process("simpleView", result);
	}
	
	@RequestMapping(method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public SimpleResponse doPost(@RequestBody SimpleRequest request)//public String doPost()
	{
		SimpleResponse result = SimpleResponse.builder()
								.status(org.springframework.http.HttpStatus.OK)
								.timeStamp(System.currentTimeMillis())
								.testVar(currentTime())
								.message("from controller")
								.build();
		log.debug( "doPost called" );

		return result;
	}

	@RequestMapping(method=RequestMethod.PUT)
	public String doPut()
	{
		log.debug( "doPut called" );
		return "this is simple PUT api" + "\n memo:"+ currentTime();
	}

	@RequestMapping(method=RequestMethod.DELETE)
	public String doDelete()
	{
		log.debug( "DELETE called" );
		return "this is simple DELETE api" + "\n memo:"+ currentTime();
	}
}