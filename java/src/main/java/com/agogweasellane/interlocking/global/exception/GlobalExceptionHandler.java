package com.agogweasellane.interlocking.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.resource.NoResourceFoundException;

import com.agogweasellane.interlocking.global.ErrorMessageEnum;
import com.agogweasellane.interlocking.global.ErrorRuleInterface;
import com.amazonaws.services.s3.model.AmazonS3Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler implements ErrorRuleInterface
{
    // @ExceptionHandler({WrongRequestException.class, WrongWorkException.class})
    // public ResponseEntity<ErrorResponse> handleAllExceptions(BaseWrongException argEx)
    @ExceptionHandler({WrongRequestException.class})
    public ResponseEntity<ErrorResponse> handleAllExceptions(WrongRequestException argEx)
    {
        String className = WrongRequestException.class.getSimpleName();
        ErrorResponse ret = setErrorResponse(HttpStatus.BAD_REQUEST, argEx.getErrorEnum().getCode(), className);
        log.error("handleAllExceptions - Exception Type: {} - {}", className, argEx.getLocalizedMessage());

        if(log.isDebugEnabled())
        {
            ret.setMessage(argEx.getMessage());
        }
        log.error("handleAllExceptions {}", argEx.getLocalizedMessage());

        return new ResponseEntity<>(ret, ret.getStatus());
    }


    @ExceptionHandler({ WrongWorkException.class, NullPointerException.class, AmazonS3Exception.class})
    public ResponseEntity<ErrorResponse> handleAllExceptions(WrongWorkException argEx)
    {
        String className = WrongWorkException.class.getSimpleName();
        ErrorResponse ret = setErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, argEx.getErrorEnum().getCode(), className);
        log.error("handleAllExceptions - Exception Type: {} - {}", className, argEx.getLocalizedMessage());

        if(log.isDebugEnabled())
        {
            ret.setMessage(argEx.getMessage());
        }
        log.error("handleAllExceptions {}", argEx.getLocalizedMessage());

        return new ResponseEntity<>(ret, ret.getStatus());
    }


    /**
     * @param arg1 존재하지 않는 컨트롤러 주소등.(404 not found)
     * @return ErrorResponse
     */
    @ExceptionHandler({ org.springframework.web.servlet.resource.NoResourceFoundException.class,
                        NoResourceFoundException.class})
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception argEx)
    {
        String className = WrongWorkException.class.getSimpleName();
        ErrorResponse ret = setErrorResponse(ErrorMessageEnum.REQUEST_NONE);
        ret.setTag(className);
        log.error("handleAllExceptions - Exception Type: {} - {}", className, argEx.getLocalizedMessage());
        if(log.isDebugEnabled())
        {
            ret.setMessage(argEx.getMessage());
        }
        log.error("handleAllExceptions {}", argEx.getLocalizedMessage());

        return new ResponseEntity<>(ret, ret.getStatus());
    }

    /**
     * @param arg1 정의된 범위외의 에러들 전부.
     * @return ErrorResponse
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Throwable argEx, WebRequest argRequest)
    {
        String className = WrongWorkException.class.getSimpleName();
        ErrorResponse ret = null;
        String packagePath = argEx.getClass().getName();
        ret = setErrorResponse(ErrorMessageEnum.ECT_EXCEPTION);
        ret.setTag(className);
        log.error("handleAllExceptions. {} - {}", packagePath, argEx.getLocalizedMessage());

        if(log.isDebugEnabled())
        {
            ret.setMessage(argEx.getMessage());
        }
        else
        {
            ret.setMessage("");
        }

        return new ResponseEntity<>(ret, ret.getStatus());
    }
}