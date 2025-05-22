package com.agogweasellane.interlocking.base.database;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BaseSkeyMongoRepository extends MongoRepository<BaseDocument, String>
{

}