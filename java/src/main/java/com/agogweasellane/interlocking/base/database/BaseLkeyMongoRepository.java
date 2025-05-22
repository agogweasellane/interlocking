package com.agogweasellane.interlocking.base.database;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BaseLkeyMongoRepository extends MongoRepository<BaseDocument, Long>
{

}