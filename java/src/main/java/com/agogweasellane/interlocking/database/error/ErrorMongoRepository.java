package com.agogweasellane.interlocking.database.error;

import org.springframework.data.mongodb.repository.MongoRepository;

//public class EchoMongoRepository implements MongoRepository<EchoDocument, String>
public interface ErrorMongoRepository extends MongoRepository<ErrorDocument, String>
{
}