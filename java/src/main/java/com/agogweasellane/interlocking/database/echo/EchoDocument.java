package com.agogweasellane.interlocking.database.echo;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.agogweasellane.interlocking.base.database.BaseSkeyDocument;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "tbl_echo")//RDBMS기준으로는 테이블이기도 하고, 레이어 레벨 오갈때 안 헷갈리게 둘을 합침.
@NoArgsConstructor
@Getter
@Setter
public class EchoDocument extends BaseSkeyDocument
{
	private String date;
	
	@Indexed(name = "ttl", expireAfter = "1s")
	private long ttl;
}