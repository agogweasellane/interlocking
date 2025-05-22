package com.agogweasellane.interlocking.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetAddress;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.agogweasellane.interlocking.controllers.api.echo.dto.EchoDto;
import com.agogweasellane.interlocking.controllers.api.echo.dto.EchoRestRequest;
import com.agogweasellane.interlocking.controllers.api.echo.dto.EchoRestResponse;
import com.agogweasellane.interlocking.database.echo.EchoDocument;
import com.agogweasellane.interlocking.database.echo.EchoEntity;
import com.agogweasellane.interlocking.database.echo.EchoRedisEntity;
import com.agogweasellane.interlocking.database.echo.EchoRepository;
import com.agogweasellane.interlocking.models.service_layer.echo.EchoMariaService;
import com.agogweasellane.interlocking.models.service_layer.echo.EchoMongoService;
import com.agogweasellane.interlocking.models.service_layer.echo.EchoRedisService;
import com.agogweasellane.interlocking.models.service_layer.external.GoogleCloudService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class EchoMvcTester extends BaseMvcTester<EchoRestRequest, EchoRestResponse>
{
	@Autowired private EchoRepository repo;
	@Autowired private EchoMariaService mService;
	@Autowired private EchoRedisService mEchoRedisService;
	@Autowired private EchoMongoService mEchoMongoService;
	@Autowired private GoogleCloudService mGoogleService;
	
	@Test
	@Override
	public void doTest() throws Exception
	{
		mUrl = "/echo";
		testGet();
		testPost();
		testPatch();
		testPut();
		testDelete();
		testRepository();
		testService();
	}

	@Override
	public void testGet() throws Exception
	{
	}

	@Override
	public void testPost() throws Exception
	{
	}

	@Override
	public void testPut() throws Exception
	{
	}

	@Override
	public void testPatch() throws Exception
	{
	}

	@Override
	public void testDelete() throws Exception
	{
	}

	@Override
	public void testRepository() throws Exception
	{
		String TEST_MSG = "TestRepository_func";
		int LOOP_SIZE = 3;
		EchoEntity insertEntity = null;

		for(int idx=0; idx<LOOP_SIZE; idx++)
		{
			insertEntity = EchoEntity.builder()
					.host_v4("localhost_" + Integer.toString(idx))
					.build();
			repo.save( insertEntity );
			
//			assertThat( insertEntity.getCreate_date() ).isNotNull();
//			assertThat( insertEntity.getCreate_timestamp().getYear() ).isGreaterThan( 0 );
		}
		assertThat( repo.count() ).isGreaterThanOrEqualTo( LOOP_SIZE );
		log.debug( "(TestRepository)repo.count()={}", repo.count() );
		
		Optional<EchoEntity> opt = repo.findById( "localhost_" + Integer.toString(0) );
		long nowStamp = System.currentTimeMillis();
		EchoEntity entityFromTable = opt.get( );
		assertThat( entityFromTable.getEdit_timestamp().getTime() ).isGreaterThanOrEqualTo( entityFromTable.getCreate_timestamp().getTime() );
		assertThat( entityFromTable.getCreate_timestamp() ).isNotNull();
		assertThat( entityFromTable.getCreate_timestamp().getTime() ).isLessThan( nowStamp );
		assertThat( entityFromTable.getEdit_timestamp() ).isNotNull();
		assertThat( entityFromTable.getEdit_timestamp().getTime() ).isLessThan( nowStamp );
	}

	@Override
	public void testService() throws Exception
	{
		//START. 레디스
		boolean flagRedis = false;
		EchoRedisEntity redisEntity = null;
		//START. 레디스
		
		//START. 몽고
		boolean flagMongo = false;
		EchoDocument mongoDoc = null;
		//END. 몽고
		
		for(int stepIdx = 0; stepIdx<2; stepIdx++)
		{
			for(int idx=0; idx<5; idx++)
			{
				redisEntity = new EchoRedisEntity();
				redisEntity.setSKey("test_" + Integer.toString(idx));
				redisEntity.setEdit_date( currentTime() );
				mEchoRedisService.insert(redisEntity, true);
				assertThat( mEchoRedisService.count()>0 ).isTrue();
				
				mongoDoc = new EchoDocument();
				mongoDoc.setSKey("test_" + Integer.toString(idx));
				mongoDoc.setDate( currentTime() );
				mongoDoc.setTtl( System.currentTimeMillis()/1000L + 1000000L );
				mEchoMongoService.insert(mongoDoc, flagRedis);
			}
			
			if(stepIdx==0)
			{
				log.debug("board_echo={}", mEchoRedisService.count());
				if(mEchoRedisService.count()>0)
				{
					flagRedis = mEchoRedisService.clean();
					assertThat( flagRedis ).isTrue();
				}
				if(mEchoMongoService.count()>0)
				{
					flagMongo = mEchoMongoService.clean();
					assertThat( flagMongo ).isTrue();
				}
			}
		}

		assertThat( mEchoMongoService.count()>0 ).isTrue();
		EchoDto dto = new EchoDto();
		dto.setHost_v4(InetAddress.getLocalHost().getHostAddress());
		assertThat( mService.update(dto) ).isTrue();
		assertThat( mService.count()>0 ).isTrue();
	}

}