package com.agogweasellane.interlocking.base.database;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BaseSkeyDocument extends BaseDocument
{
	@Id
	protected String sKey;
}