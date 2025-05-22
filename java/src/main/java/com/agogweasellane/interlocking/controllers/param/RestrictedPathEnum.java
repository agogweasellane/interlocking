package com.agogweasellane.interlocking.controllers.param;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RestrictedPathEnum
{
    SIMPLE(RestrictedParam.LOCAL_SIMPLE),
    ECHO(RestrictedParam.LOCAL_ECHO);

    private final String path;
}