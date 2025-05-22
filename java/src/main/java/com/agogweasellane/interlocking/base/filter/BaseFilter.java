package com.agogweasellane.interlocking.base.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.agogweasellane.interlocking.framwork.CachedHttpServletRequest;
import com.agogweasellane.interlocking.global.ErrorMessageEnum;
import com.agogweasellane.interlocking.global.exception.ErrorResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseFilter extends OncePerRequestFilter 
{
	protected boolean isCachedServletRequest(HttpServletRequest request)
	{
		boolean result = false;
		
		if(request instanceof CachedHttpServletRequest)
		{
			result = true;
		}
		else
		{
			log.error("FilterConfiguration order check");
		}
		
		return result;
	}
	
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
    	log.debug("BaseFilter.doFilterInternal");
        throw new UnsupportedOperationException("Unimplemented method 'doFilterInternal'");
    }

    protected ContentCachingResponseWrapper setErrorCachingResponse(HttpServletResponse argServletRes, ErrorMessageEnum argEnum, String errLog)
    {
        ErrorResponse errBuilder = ErrorResponse
        .builder()
        .status(argEnum.getStatus())
        .message( (errLog==null)? argEnum.getStatus().name():errLog )
        .build();

        return this.setErrorCachingResponse(argServletRes, errBuilder);
    }

    protected ContentCachingResponseWrapper setErrorCachingResponse(HttpServletResponse argServletRes , ErrorResponse argErr)
    {
/*
        reult의 멤버함수 입력사항만 반복.
        여기서는 ContentCachingResponseWrapper.copyBodyToResponse() NG!!!
*/
        ContentCachingResponseWrapper reult = null;

        reult = new ContentCachingResponseWrapper(argServletRes);
        reult.setStatus(argErr.getStatus().value());
        reult.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE); // 내용 정보
        reult.setHeader("Pragma", "no-cache"); // HTTP 1.0 캐싱 방지
        reult.setContentType( MediaType.APPLICATION_JSON_VALUE );
        reult.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            reult.getWriter().write(argErr.doSeserialize());
        } catch (IOException e) {
            log.error(this.getClass().getName(), e);
        }

        return reult;
    }
}