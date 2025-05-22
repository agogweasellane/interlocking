package com.agogweasellane.interlocking.framwork.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.agogweasellane.interlocking.base.ApiMethodEnum;
import com.agogweasellane.interlocking.base.controller.BaseRequest;
import com.agogweasellane.interlocking.base.filter.BaseFilter;
import com.agogweasellane.interlocking.controllers.param.RestrictedParam;
import com.agogweasellane.interlocking.global.ErrorMessageEnum;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthFilter extends BaseFilter//OncePerRequestFilter 
{
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
    								@NonNull HttpServletResponse response,
    								@NonNull FilterChain filterChain)
    throws ServletException, IOException
    {
    	log.debug("doFilterInternal");
        ContentCachingResponseWrapper cachingResponse = null;
    	isCachedServletRequest(request);


		String requestURI = request.getRequestURI();
        ApiMethodEnum methodByCall = null;
        try {
            methodByCall = ApiMethodEnum.valueOf(request.getMethod());
        } catch (IllegalArgumentException  e) {
            log.error(requestURI, e);
        }

        if(requestURI.endsWith(RestrictedParam.FORM_DATA))
        {
            //do-nothing
        }
        else if(methodByCall.isRequired()==true)
        {//CASE. 리퀘스트 body에 method가 반드시 명시
        	if(log.isDebugEnabled())
            {
        		log.debug( IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8) );
            }

            BaseRequest requestBuilder = new BaseRequest();
            requestBuilder = requestBuilder.doDeserialize(request.getInputStream());//doFilter전에 content가 제대로 보이는건 getInputStream
            if(log.isDebugEnabled())
            {
                log.debug( requestBuilder.doSeserialize() );
            }

            if(requestBuilder == null
                || requestBuilder.getMethod() == null 
                || methodByCall.name().equalsIgnoreCase(requestBuilder.getMethod().name()) == false)
            {
                log.debug( "response isCommitted = {}", Boolean.toString(response.isCommitted()) );
                cachingResponse = this.setErrorCachingResponse(response, ErrorMessageEnum.REQUEST_METHOD, "wrong format");
            }
        }


        if(cachingResponse==null)   {    filterChain.doFilter(request , response);/*WARN. 캐싱로직상, doFilter이전에 request.스트림 절대 금지 */    }
        else                        {    cachingResponse.copyBodyToResponse();   }
        if(log.isDebugEnabled())
        {
            log.debug( "response isCommitted = " + Boolean.toString(response.isCommitted()) );
        }
    }

    @Override
    public void destroy()
    {
    	log.debug("AuthFilter.destroy");
    }
}
/*
2020-국문
    https://leeyongjin.tistory.com/entry/request-response-logging

2024-국문
    https://velog.io/@dlwlrma/알고-쓰자-ContentCachingRequestWrapper
*/