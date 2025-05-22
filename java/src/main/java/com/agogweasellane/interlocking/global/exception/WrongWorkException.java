package com.agogweasellane.interlocking.global.exception;

import com.agogweasellane.interlocking.base.BaseWrongException;
import com.agogweasellane.interlocking.global.ErrorMessageEnum;

public class WrongWorkException extends BaseWrongException
{
    public WrongWorkException(ErrorMessageEnum argEnum, String argMsg)
    {
        super(argEnum, argMsg);
    }
    public WrongWorkException(ErrorMessageEnum argEnum)
    {
        super(argEnum, "");
    }
    public WrongWorkException(String argMsg)
    {
        super(ErrorMessageEnum.SERVICE_WRONG, argMsg);
    }
}
