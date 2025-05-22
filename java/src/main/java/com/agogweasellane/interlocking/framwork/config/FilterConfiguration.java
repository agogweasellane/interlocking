package com.agogweasellane.interlocking.framwork.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.agogweasellane.interlocking.framwork.filter.AuthFilter;
import com.agogweasellane.interlocking.framwork.filter.CachingFilter;

import jakarta.servlet.Filter;

@Configuration
public class FilterConfiguration implements WebMvcConfigurer
{
    @Bean
    public FilterRegistrationBean cachingFilter()
    {
        FilterRegistrationBean<Filter> regBean = new FilterRegistrationBean<>();
        regBean.setFilter(new CachingFilter());
        regBean.setOrder(1);//setOrder = 1-base
        //regBean.addUrlPatterns( RestrictedParam.LOCAL_SIMPLE );
        regBean.addUrlPatterns( "/*" );

        return regBean;
    }
    @Bean
    public FilterRegistrationBean authFilter()
    {
        FilterRegistrationBean<Filter> regBean = new FilterRegistrationBean<>();
        regBean.setFilter(new AuthFilter());
        regBean.setOrder(2);//setOrder = 1-base
        //regBean.addUrlPatterns( RestrictedParam.LOCAL_SIMPLE );
        regBean.addUrlPatterns( "/*" );

        return regBean;
    }
}
/*
2021-국문
    https://veneas.tistory.com/entry/Spring-Boot-Filter-를-이용하여-Response-Body-핸들링-HttpServletResponseWrapper
    https://velog.io/@ansalstmd/스프링부트-다양한-기능-5.-Spring-Boot-Filter와-Interceptor
    https://pgnt.tistory.com/entry/SpringBoot-Springboot-Filter-OncePerRequestFilter간단히-사용하기
2021-영문
    https://www.baeldung.com/spring-boot-filter-response-body
        https://github.com/eugenp/tutorials/tree/master/spring-boot-modules/spring-boot-basic-customization-3
    https://www.baeldung.com/spring-reading-httpservletrequest-multiple-times

2023
    https://velog.io/@ksk7584/Filter를-등록하는-4가지-방법
*/