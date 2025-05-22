package com.agogweasellane.interlocking.base.database;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BaseLkeyDocument extends BaseDocument
{
	@Id
	protected long lKey;
}