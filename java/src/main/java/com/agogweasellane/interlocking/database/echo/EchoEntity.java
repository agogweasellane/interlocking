package com.agogweasellane.interlocking.database.echo;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.agogweasellane.interlocking.base.database.BaseMongoEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_echo")
@EntityListeners(AuditingEntityListener.class)
public class EchoEntity extends BaseMongoEntity
{
	@Id
	private String host_v4 = "nnn.nnn.nnn.nnn";//빌더라서 가능하니.
	
	@Column(nullable = true)
	private String host_v6;
	
	@CreatedDate
	@Column(updatable = false)
    private LocalDateTime create_date;

    @LastModifiedDate
    @Column(updatable = true)
    private LocalDateTime edit_date;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp create_timestamp;
	
	@UpdateTimestamp
	@Column(updatable = true)
	private Timestamp edit_timestamp;
}