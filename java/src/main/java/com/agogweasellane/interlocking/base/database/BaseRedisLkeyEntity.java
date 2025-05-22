package com.agogweasellane.interlocking.base.database;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class BaseRedisLkeyEntity extends BaseRedisEntity
{
	@Id
	protected long lKey;
}