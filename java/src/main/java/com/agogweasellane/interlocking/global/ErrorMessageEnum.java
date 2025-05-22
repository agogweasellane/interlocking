package com.agogweasellane.interlocking.global;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessageEnum
{
	//	client <--> controller
	//BAD_REQUEST(400, Series.CLIENT_ERROR, "Bad Request"),
	REQUEST_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "da_eod_wng", "method in request is wrong."),
	REQUEST_PREV_VALUE(HttpStatus.BAD_REQUEST, "da_ong_lue", "value is not latest."),
	REQUEST_PREV_VERSIOR(HttpStatus.UPGRADE_REQUIRED, "da_ont_ltes", "update your client."),
	REQUEST_WRONG(HttpStatus.NOT_ACCEPTABLE, "da_eod_wng", "request is wrong."),
	REQUEST_NONE(HttpStatus.NOT_FOUND, "da_fud_wng", "404 error"),

	//비즈니스 <->스토리지
	SERVICE_WRONG(HttpStatus.SERVICE_UNAVAILABLE, "xt_sce_aabe", "service is out."),


	
	//정의 되지않은 나머지 에러.
	ECT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "un_oree", "undefined error. check log"),
	
	
	NONE(HttpStatus.OK, "do_nothing", "해당 enum는 에러로그 로직 확인용");
	
	
	private final HttpStatus status;
	private final String code;
	private final String desc;
}