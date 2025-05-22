package com.agogweasellane.interlocking.database.echo;

import org.springframework.data.redis.core.RedisHash;

import com.agogweasellane.interlocking.base.database.BaseRedisSkeyEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RedisHash("board_echo")
public class EchoRedisEntity extends BaseRedisSkeyEntity
{
	private String edit_date;
}