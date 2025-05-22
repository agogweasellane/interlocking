package com.agogweasellane.interlocking.controllers.api.echo.dto;

import com.agogweasellane.interlocking.base.controller.BaseResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor//defaultConstructor
@Getter//WARN. 자식 리스폰스여서 신규 필드에 대해 필수.
public class EchoRestResponse extends BaseResponse<EchoRestResponse>
{
	private boolean mariadbHealth;
	private boolean redisHealth;
	private boolean mongoHealth;
	private boolean googleCloudHealth;
	private boolean s3Health;
}