package com.agogweasellane.interlocking.base.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.agogweasellane.interlocking.component.UtilityComponent;
import com.agogweasellane.interlocking.controllers.FileDto;
import com.agogweasellane.interlocking.controllers.api.echo.dto.EchoRestRequest;
import com.agogweasellane.interlocking.controllers.param.RestrictedParam;
import com.agogweasellane.interlocking.database.error.ErrorDocument;
import com.agogweasellane.interlocking.global.DefaultInterface;
import com.agogweasellane.interlocking.global.ErrorMessageEnum;
import com.agogweasellane.interlocking.global.ErrorRuleInterface;
import com.agogweasellane.interlocking.global.exception.WrongRequestException;
import com.agogweasellane.interlocking.global.exception.WrongWorkException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;



@SuppressWarnings("rawtypes")
@Slf4j
@RestController
@Tag( name = "", description =  "화면 생성이 아닌 JSON/XML같은 리스폰스를 내려주려면 @ResponseBody를 기본으로 처리하는 @RestController를 쓰는쪽이 수월")
public abstract class BaseRestController<I extends BaseRequest, O extends BaseResponse>
										implements ErrorRuleInterface
{
/*
DOC. 설계 및 표준 관련
	멱등성(idempotentency) X: CONNECT, POST, PATCH
	멱등성(idempotentency) O: viewOnly가 되게끔.

REF.
	https://docs.tosspayments.com/blog/rest-api-post-put-patch
*/

	//START. 생성자 주입용
	protected final SpringTemplateEngine mTemplateEngine;
	protected final ApplicationContext mAppContext;
	//END. 생성자 주입용

	public abstract O doControll(I argRequest);//TO-DO.사용 및 요구사항이 누적되면 코드 고도화&공용구간에 활용토록. [2025-01-10]
	public abstract O getControll(HashMap<String,Object> argRequest) throws WrongRequestException, WrongWorkException;
	public abstract O postControll(I argRequest) throws WrongRequestException, WrongWorkException;
	public abstract O putControll(I argRequest) throws WrongRequestException, WrongWorkException;
	public abstract O patchControll(I argRequest) throws WrongRequestException, WrongWorkException;
	public abstract O deleteControll(I argRequest) throws WrongRequestException, WrongWorkException;
	//MEMO. 추후 xxxControll이 추가될 경우, 가급적 해당 클래스에.

	@Autowired private UtilityComponent mUtilityComponent;


	@Autowired
	public BaseRestController(ApplicationContext argAppContext, SpringTemplateEngine argTemplate)
	{//CASE. 생성자 주입
		this.mAppContext = argAppContext;
		this.mTemplateEngine = argTemplate;
		//this.mService = argService;
	}
	
	
	protected String currentTime()
	{
		String result = null;
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-DD HH:mm:ss");
		formatter.withLocale( Locale.KOREA );
		
		return now.format(formatter);
	}
	
	protected void insertErrorlog(ErrorMessageEnum argErr, String argMsg)
	{
		ErrorDocument doc = new ErrorDocument();
		doc.setPath(this.getClass().getSimpleName());
		doc.setEnumName(argErr.name());
		doc.setEnumCode(argErr.getCode());
		doc.setMsg(argMsg);
		
		StringBuilder pkBuilder = new StringBuilder();
		pkBuilder
		.append(doc.getPath()).append(DefaultInterface.TOKEN_PK).append( System.currentTimeMillis() );
		doc.setSKey(pkBuilder.toString());
	}
	
	protected byte[] mapIntoBytes(HashMap<String, Object> map) throws JsonProcessingException
	{
		byte[] result;
		String jsonString = new ObjectMapper().writeValueAsString(map);
		result = jsonString.getBytes(StandardCharsets.UTF_8);

		return result;
	}


	@RequestMapping(method=RequestMethod.GET)
	protected O doGet(@RequestParam HashMap<String,Object> argMap) throws WrongRequestException, WrongWorkException
	{//viewOnly!!!
		O result = null;
		String className = this.getClass().getSimpleName();
		if(log.isDebugEnabled())	{	log.debug( "(class:{})argRequest={}", className, (argMap==null)? "empty":argMap.toString() );	}

		result = this.getControll( argMap );
		result.setStatus(HttpStatus.OK);
		result.setTag(HttpStatus.OK.name());
		result.setCode(HttpStatus.OK.name());

		return result;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
			     produces = MediaType.APPLICATION_JSON_VALUE)//since spring 4.3
	protected O doPost(@RequestBody I argRequest) throws WrongRequestException, WrongWorkException
	{//[멱등성 X]	리소스 생성 
		O result = null;
		String className = this.getClass().getSimpleName();
		if(log.isDebugEnabled())	{	log.debug( "(class:{})argRequest={}", className, argRequest.doSeserialize() );	}

		result = this.postControll( argRequest );
		result.setStatus(HttpStatus.OK);
		result.setTag(HttpStatus.OK.name());
		result.setCode(HttpStatus.OK.name());

		return result;
	}
	
	@PostMapping(path=RestrictedParam.FORM_DATA,
				 consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
			     produces = MediaType.APPLICATION_JSON_VALUE)//since spring 4.3
	protected O doPostWithFiles(@RequestPart(value = "json") I argRequest,
								@RequestPart(value = "files", required = true) MultipartFile[] argFiles) throws WrongRequestException, WrongWorkException
	{//[멱등성 X]	리소스 생성 
		O result = null;
		String className = this.getClass().getSimpleName();
		if(log.isDebugEnabled())	{	log.debug( "(class:{})argRequest={}", className, argRequest.doSeserialize() );	}
	

		if(argFiles==null || argFiles.length==0){throw new WrongRequestException(ErrorMessageEnum.REQUEST_WRONG, "file null(1)");}

		FileDto[] convertedFiles = mUtilityComponent.multipartFileToFileDto(argFiles);
		if(convertedFiles.length!=argFiles.length)
		{
			throw new WrongWorkException("wrong files... retry please");
		}

		//첨부파일을 처리하는 API의 종류가 제한적인만큼 instanceof. 어느정도 볼륨이 쌓이고 나면 멀티폼용 베이스 클래스가 나을지도? [TO-DO]
		if (argRequest instanceof EchoRestRequest)
		{
			((EchoRestRequest) argRequest).setFiles(convertedFiles);
		}
		else
		{
			throw new WrongRequestException(ErrorMessageEnum.REQUEST_WRONG, "not file support api");
		}

		result = this.postControll( argRequest );
		result.setStatus(HttpStatus.OK);
		result.setTag(HttpStatus.OK.name());
		result.setCode(HttpStatus.OK.name());

		return result;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	protected O doPut(@RequestBody I argRequest) throws WrongRequestException, WrongWorkException
	{//[멱등성 O]	전체 업데이트.
		O result = null;
		String className = this.getClass().getSimpleName();
		if(log.isDebugEnabled())	{	log.debug( "(class:{})argRequest={}", className, argRequest.doSeserialize() );	}

		result = this.putControll( argRequest );
		result.setStatus(HttpStatus.OK);
		result.setTag(HttpStatus.OK.name());
		result.setCode(HttpStatus.OK.name());

		return result;
	}

	@PutMapping(path=RestrictedParam.FORM_DATA,
				 consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
			     produces = MediaType.APPLICATION_JSON_VALUE)//since spring 4.3
	protected O doPutWithFiles(@RequestPart(value = "json") I argRequest,
							   @RequestPart(value = "files", required = true) MultipartFile[] argFiles) throws WrongRequestException, WrongWorkException
	{
		O result = null;
		String className = this.getClass().getSimpleName();


		result = this.putControll( argRequest );
		result.setStatus(HttpStatus.OK);
		result.setTag(HttpStatus.OK.name());
		result.setCode(HttpStatus.OK.name());

		return result;
	}


	@RequestMapping(method=RequestMethod.PATCH)
	protected O doPatch(@RequestBody I argRequest) throws WrongRequestException, WrongWorkException
	{//[멱등성 X]	부분 업데이트. 변경하고 싶지 않은 필드는 요청 본문에서 생략허용.
		O result = null;
		String className = this.getClass().getSimpleName();
		if(log.isDebugEnabled())	{	log.debug( "(class:{})argRequest={}", className, argRequest.doSeserialize() );	}

		result = this.patchControll( argRequest );
		result.setStatus(HttpStatus.OK);
		result.setTag(HttpStatus.OK.name());
		result.setCode(HttpStatus.OK.name());

		return result;
	}	
	@PatchMapping(path=RestrictedParam.FORM_DATA,
				 consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
			     produces = MediaType.APPLICATION_JSON_VALUE)//since spring 4.3
	protected O doPatchWithFiles(@RequestPart(value = "json") I argRequest,
							   @RequestPart(value = "files", required = true) MultipartFile[] argFiles) throws WrongRequestException, WrongWorkException
	{
		O result = null;
		String className = this.getClass().getSimpleName();


		result = this.patchControll( argRequest );
		result.setStatus(HttpStatus.OK);
		result.setTag(HttpStatus.OK.name());
		result.setCode(HttpStatus.OK.name());

		return result;
	}


	@RequestMapping(method=RequestMethod.DELETE)
	protected O doDelete(@RequestBody I argRequest) throws WrongRequestException, WrongWorkException
	{
		O result = null;
		String className = this.getClass().getSimpleName();

		result = deleteControll( argRequest );
		result.setStatus(HttpStatus.OK);
		result.setTag(HttpStatus.OK.name());
		result.setCode(HttpStatus.OK.name());

		return result;
	}
}