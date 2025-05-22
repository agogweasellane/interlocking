package com.agogweasellane.interlocking.database.error;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.agogweasellane.interlocking.base.database.BaseSkeyDocument;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "tbl_error_log")//RDBMS기준으로는 테이블이기도 하고, 레이어 레벨 오갈때 안 헷갈리게 둘을 합침.
@NoArgsConstructor
@Getter
@Setter
public class ErrorDocument extends BaseSkeyDocument
{
	private String path;
	private String enumName;
	private String enumCode;
	private String msg;

	@CreatedDate
	@Column(updatable = false)
    private LocalDateTime date;

	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp timestamp;
}