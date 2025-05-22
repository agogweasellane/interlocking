package com.agogweasellane.interlocking.framwork.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.lang.NonNull;

import com.agogweasellane.interlocking.base.filter.BaseFilter;
import com.agogweasellane.interlocking.controllers.param.RestrictedParam;
import com.agogweasellane.interlocking.framwork.CachedHttpServletRequest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CachingFilter extends BaseFilter
{
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
    								@NonNull HttpServletResponse response,
    								@NonNull FilterChain filterChain)
    throws ServletException, IOException
    {
        String requestURI = request.getRequestURI();
        if(requestURI.endsWith(RestrictedParam.FORM_DATA))
        {
            filterChain.doFilter(request, response);
            return;
        }

    	CachedHttpServletRequest wrappedRequest = new CachedHttpServletRequest((HttpServletRequest) request);

    	if(log.isDebugEnabled())
    	{
        	//isCachedServletRequest(request);
        	isCachedServletRequest(wrappedRequest);
            log.debug( new String(wrappedRequest.getInputStream().readAllBytes(), StandardCharsets.UTF_8) );
    	}
    	
        filterChain.doFilter(wrappedRequest, response);
    }

    @Override
    public void destroy()
    {
    	log.debug("AuthFilter.destroy");
    }
}