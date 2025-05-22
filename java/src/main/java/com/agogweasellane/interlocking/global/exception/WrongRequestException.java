package com.agogweasellane.interlocking.global.exception;

import com.agogweasellane.interlocking.base.BaseWrongException;
import com.agogweasellane.interlocking.global.ErrorMessageEnum;

public class WrongRequestException extends BaseWrongException
{
    public WrongRequestException(ErrorMessageEnum argErrorEnum, String argMsg)
    {
        super(argErrorEnum, argMsg);
    }

    public WrongRequestException(String argMsg)
    {
        super(ErrorMessageEnum.REQUEST_WRONG, argMsg);
    }
}