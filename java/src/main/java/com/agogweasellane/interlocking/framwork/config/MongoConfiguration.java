package com.agogweasellane.interlocking.framwork.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class MongoConfiguration implements InitializingBean
{
    @Autowired
    @Lazy
    private MappingMongoConverter mappingMongoConverter;

    @Override
    public void afterPropertiesSet() throws Exception
    {//https://stackoverflow.com/questions/23517977/spring-boot-mongodb-how-to-remove-the-class-column
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }
}