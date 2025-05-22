package com.agogweasellane.interlocking.models.service_layer.echo;

import org.springframework.stereotype.Service;

import com.agogweasellane.interlocking.base.service.BaseMongoService;
import com.agogweasellane.interlocking.database.error.ErrorDocument;
import com.agogweasellane.interlocking.database.error.ErrorMongoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ErrorLogMongoService extends BaseMongoService<ErrorMongoRepository, ErrorDocument>
{
	public ErrorLogMongoService(ErrorMongoRepository mRepository)
	{
		super(mRepository);
	}
}