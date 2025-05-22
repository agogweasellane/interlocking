package com.agogweasellane.interlocking.base;

import org.springframework.http.HttpStatus;

import com.agogweasellane.interlocking.global.ErrorMessageEnum;

import lombok.Getter;

@Getter
public class BaseWrongException extends Exception
{
    protected ErrorMessageEnum errorEnum;
    protected String message;

    BaseWrongException()
    {
    }
    public BaseWrongException(ErrorMessageEnum argErrorEnum, String argMsg)
    {
        super(argErrorEnum.getCode());
        errorEnum = argErrorEnum;
        message = argMsg;
    }

    // public HttpStatus getStatus()
    // {
    //     return mErrorEnum.getStatus();
    // }
}