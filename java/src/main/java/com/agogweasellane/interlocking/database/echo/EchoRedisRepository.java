package com.agogweasellane.interlocking.database.echo;

import org.springframework.data.repository.CrudRepository;

public interface EchoRedisRepository extends CrudRepository<EchoRedisEntity, String>
{

}