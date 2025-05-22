package com.agogweasellane.interlocking.controllers.api.echo;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.agogweasellane.interlocking.base.controller.BaseRestController;
import com.agogweasellane.interlocking.controllers.api.echo.dto.EchoRestRequest;
import com.agogweasellane.interlocking.controllers.api.echo.dto.EchoRestResponse;
import com.agogweasellane.interlocking.controllers.param.RestrictedParam;
import com.agogweasellane.interlocking.global.exception.WrongWorkException;
import com.agogweasellane.interlocking.models.service_layer.echo.EchoMariaService;
import com.agogweasellane.interlocking.models.service_layer.echo.EchoMongoService;
import com.agogweasellane.interlocking.models.service_layer.echo.EchoRedisService;
import com.agogweasellane.interlocking.models.service_layer.external.GoogleCloudService;
import com.agogweasellane.interlocking.models.service_layer.external.aws.AwsS3Service;
import com.agogweasellane.interlocking.models.service_layer.external.aws.S3BucketEnum;
import com.agogweasellane.interlocking.models.service_layer.external.aws.S3ObjectDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping(value = RestrictedParam.LOCAL_ECHO)//, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name="EchoRestController", description = "healthCheck. 단위 테스트 및 기본 동작 확인용 컨트롤러")
public class EchoRestController extends BaseRestController<EchoRestRequest, EchoRestResponse>
{
	@Autowired private EchoRedisService mEchoRedisService;
	@Autowired private EchoMongoService mEchoMongoService;
	@Autowired private EchoMariaService mEchoMariaService;
	@Autowired private GoogleCloudService mGoogleService;
	@Autowired private AwsS3Service mAwsS3Service;
	//@Autowired private Files mGoogleFiles;


	public EchoRestController(ApplicationContext argAppContext, SpringTemplateEngine argTemplate)
	{
		super( argAppContext, argTemplate);
	}

	@Override
	public EchoRestResponse doControll(EchoRestRequest argRequest)
	{
		return null;
	}

	@Override
	public EchoRestResponse getControll(HashMap<String,Object> argRequest) throws WrongWorkException
	{
		long fileCount = -1;//mGoogleService.count();
		
		StringBuilder logBuilder = new StringBuilder();
		logBuilder
		.append("Mariadb=").append(mEchoMariaService.count())
		.append(" / ").append("Mongo=").append(mEchoMongoService.count())
		.append(" / ").append("Redis=").append(mEchoRedisService.count())
		.append(" / ").append("GoogleFiles=").append(fileCount);
		
		EchoRestResponse result = EchoRestResponse.builder()
									.mariadbHealth( mEchoMariaService.isAvailable() )
									.redisHealth( mEchoRedisService.isAvailable() )
									.mongoHealth( mEchoMongoService.isAvailable() )
									.s3Health( mAwsS3Service.isAvailable() )
									.googleCloudHealth( (fileCount==0)?false:true )
									.message( logBuilder.toString() )
									.build();

		if(result.isMariadbHealth()==false){throw new WrongWorkException("isMariadbHealth()==false");}
		if(result.isMongoHealth()==false){	throw new WrongWorkException("isMongoHealth()==false");}
		if(result.isRedisHealth()==false){	throw new WrongWorkException("isRedisHealth()==false");}
		if(result.isS3Health()==false)	 {	throw new WrongWorkException("isS3Health()==false");}


		return result;
	}

	@Operation(summary = "POST api", description = "파일 신규입력만 가능")
	@Override
	public EchoRestResponse postControll(EchoRestRequest argRequest)
	{
		EchoRestResponse result = EchoRestResponse.builder().status(HttpStatus.OK)
													.mariadbHealth( mEchoMariaService.isAvailable() )
													.redisHealth( mEchoRedisService.isAvailable() )
													.mongoHealth( mEchoMongoService.isAvailable() )
													.s3Health( mAwsS3Service.isAvailable() )
													.googleCloudHealth(false)
													.build();
		StringBuilder logBuilder = new StringBuilder();


		if(argRequest.getFiles() != null && argRequest.getFiles().length>0)
		{
			final int LENG = argRequest.getFiles().length;
			S3ObjectDto[] files = new S3ObjectDto[LENG];
			for(int idx=0; idx<LENG; idx++)
			{
				files[idx] = S3ObjectDto.builder()
										.path(S3BucketEnum.path_test.getValue()).file(argRequest.getFiles()[idx])
										.build(); 
			}
			mAwsS3Service.insert(false, files);

			S3ObjectDto dtoFroms3 = mAwsS3Service.findItem(files[0]);
			if(dtoFroms3==null)							{	logBuilder.append("(private)S3.insert WRONG / ");	}

			for(int idx=0; idx<LENG; idx++)
			{
				files[idx] = S3ObjectDto.builder().root(S3BucketEnum.root_public)
										.file(argRequest.getFiles()[idx])
										.path(S3BucketEnum.path_test.getValue())
										.build(); 
			}

			mAwsS3Service.insert(false, files);
			dtoFroms3 = mAwsS3Service.findItem(files[0]);
			if(dtoFroms3==null)							{	logBuilder.append("(public)S3.insert WRONG / ");	}
		}

		return result;
	}

	@Operation(summary = "PUT api", description = "기존꺼 OW")
	@Override
	public EchoRestResponse putControll(EchoRestRequest argRequest)
	{
		EchoRestResponse result = EchoRestResponse.builder()
													.mariadbHealth( mEchoMariaService.isAvailable() )
													.redisHealth( mEchoRedisService.isAvailable() )
													.mongoHealth( mEchoMongoService.isAvailable() )
													.s3Health( mAwsS3Service.isAvailable() )
													.googleCloudHealth(false)
													.build();
		StringBuilder logBuilder = new StringBuilder();


		if(argRequest.getFiles() != null && argRequest.getFiles().length>0)
		{
			final int LENG = argRequest.getFiles().length;
			S3ObjectDto[] files = new S3ObjectDto[LENG];
			for(int idx=0; idx<LENG; idx++)
			{
				files[idx] = S3ObjectDto.builder()
										.path(S3BucketEnum.path_test.getValue()).file(argRequest.getFiles()[idx])
										.build(); 
			}
			mAwsS3Service.insert(true, files);

			S3ObjectDto dtoFroms3 = mAwsS3Service.findItem(files[0]);
			if(dtoFroms3==null)	{	logBuilder.append("(private)S3.insert WRONG / ");	}

			for(int idx=0; idx<LENG; idx++)
			{
				files[idx] = S3ObjectDto.builder().root(S3BucketEnum.root_public)
										.file(argRequest.getFiles()[idx])
										.path(S3BucketEnum.path_test.getValue())
										.build(); 
			}

			mAwsS3Service.insert(true, files);
			dtoFroms3 = mAwsS3Service.findItem(files[0]);
			if(dtoFroms3==null)	{	logBuilder.append("(public)S3.insert WRONG / ");	}
		}

		return result;
	}

	@Operation(summary = "PATCH api", description = "일부만 갱신")
	@Override
	public EchoRestResponse patchControll(EchoRestRequest argRequest)
	{
		EchoRestResponse result = EchoRestResponse.builder().status(HttpStatus.OK)
													.mariadbHealth( mEchoMariaService.isAvailable() )
													.redisHealth( mEchoRedisService.isAvailable() )
													.mongoHealth( mEchoMongoService.isAvailable() )
													.s3Health( mAwsS3Service.isAvailable() )
													.googleCloudHealth(false)
													.build();
		StringBuilder logBuilder = new StringBuilder();

		if(argRequest.getFiles() != null && argRequest.getFiles().length>0)
		{
			final int LENG = argRequest.getFiles().length;
			S3ObjectDto[] files = new S3ObjectDto[LENG];
			for(int idx=0; idx<LENG; idx++)
			{
				files[idx] = S3ObjectDto.builder()
										.path(S3BucketEnum.path_test.getValue()).file(argRequest.getFiles()[idx])
										.build(); 
			}
			if(mAwsS3Service.delete(files[0])==false)	{	logBuilder.append("(private)S3.delete WRONG / ");	}
			mAwsS3Service.insert(false, files);

			S3ObjectDto dtoFroms3 = mAwsS3Service.findItem(files[0]);
			if(dtoFroms3==null)							{	logBuilder.append("(private)S3.insert WRONG / ");	}

			for(int idx=0; idx<LENG; idx++)
			{
				files[idx] = S3ObjectDto.builder().root(S3BucketEnum.root_public)
										.file(argRequest.getFiles()[idx])
										.path(S3BucketEnum.path_test.getValue())
										.build(); 
			}
			if(mAwsS3Service.delete(files[0])==false)	{	logBuilder.append("(private)S3.delete WRONG / ");	}
			mAwsS3Service.insert(false, files);

			dtoFroms3 = mAwsS3Service.findItem(files[0]);
			if(dtoFroms3==null)							{	logBuilder.append("(public)S3.insert WRONG / ");	}
		}

		return result;
	}

	@Override
	public EchoRestResponse deleteControll(EchoRestRequest argRequest)
	{
		EchoRestResponse result = EchoRestResponse.builder().status(HttpStatus.OK)
													.mariadbHealth( mEchoMariaService.isAvailable() )
													.redisHealth( mEchoRedisService.isAvailable() )
													.mongoHealth( mEchoMongoService.isAvailable() )
													.s3Health( mAwsS3Service.isAvailable() )
													.googleCloudHealth(false)
													.build();
		StringBuilder logBuilder = new StringBuilder();

		return result;
	}
}