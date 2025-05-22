package com.agogweasellane.interlocking.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiMethodEnum
{
    GET(false),
    POST(false),
    PUT(false),
    PATCH(false),
    DELETE(true)
    ;

    private final boolean isRequired;//리퀘스트body.json에 method가 반드시 명시되어야 하는가?

    // ApiMethodEnum(boolean isRequired)
    // {
    //     this.isRequired = isRequired;
    // }
}
/*
	Request has payload body==Optional여도 true
*/