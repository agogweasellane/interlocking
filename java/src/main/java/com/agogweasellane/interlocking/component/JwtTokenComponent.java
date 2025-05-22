package com.agogweasellane.interlocking.component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenComponent
{
	public String getEncode(Algorithm argAlg, int arghour)
	{
		if(arghour<0) {	arghour = 1;	}
		
		long currentTimeStamp = System.currentTimeMillis() / 1000L;
		long addSeconds = arghour * 60L * 60L;
		
		LocalDateTime currentDate = LocalDateTime.now();
		String result = JWT.create()
						        .withExpiresAt( Date.from(Instant.ofEpochSecond(currentTimeStamp + addSeconds))  )
						        .sign(argAlg);
		
		return result;
	}
	
	public DecodedJWT getDecode(Algorithm argAlg)
	{
		DecodedJWT result = null;
		
		JWTVerifier verifier = JWT.require(argAlg)
			        .withClaim("number-claim", 123)
			        .withClaimPresence("some-claim-that-just-needs-to-be-present")
			        .withClaim("predicate-claim", (claim, decodedJWT) -> "custom value".equals(claim.asString()))
			        .build();
		result = verifier.verify("my.jwt.token");
		
		
		return result;
	}
}