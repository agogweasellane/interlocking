package com.agogweasellane.interlocking.database.echo;

import org.springframework.data.mongodb.repository.MongoRepository;

//public class EchoMongoRepository implements MongoRepository<EchoDocument, String>
public interface EchoMongoRepository extends MongoRepository<EchoDocument, String>
{
}