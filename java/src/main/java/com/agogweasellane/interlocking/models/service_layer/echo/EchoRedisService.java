package com.agogweasellane.interlocking.models.service_layer.echo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.agogweasellane.interlocking.base.IhealthCheckable;
import com.agogweasellane.interlocking.base.service.BaseRedisService;
import com.agogweasellane.interlocking.database.echo.EchoRedisEntity;
import com.agogweasellane.interlocking.database.echo.EchoRedisRepository;

@Service
public class EchoRedisService extends BaseRedisService<EchoRedisRepository, EchoRedisEntity>
							  implements IhealthCheckable
{
	public EchoRedisService(EchoRedisRepository mRepo)
	{
		super(mRepo);
	}

	@Override
	public boolean isAvailable()
	{
		EchoRedisEntity redisEntity = null;
		final int LOOP = 5;

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-DD HH:mm:ss");
		formatter.withLocale( Locale.KOREA );

		clean();
		for(int idx=0; idx<LOOP; idx++)
		{
			redisEntity = new EchoRedisEntity();
			redisEntity.setSKey("test_" + Integer.toString(idx));
			redisEntity.setEdit_date( now.format(formatter) );
			insert(redisEntity, true);
		}
		if(count()<LOOP){return false;}


		return true;
	}
}