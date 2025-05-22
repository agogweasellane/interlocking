package com.agogweasellane.interlocking.database.echo;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public interface EchoRepository extends JpaRepository<EchoEntity, String>
{

}