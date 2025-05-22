package com.agogweasellane.interlocking.models.service_layer.echo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.agogweasellane.interlocking.base.IhealthCheckable;
import com.agogweasellane.interlocking.base.service.BaseMongoService;
import com.agogweasellane.interlocking.database.echo.EchoDocument;
import com.agogweasellane.interlocking.database.echo.EchoMongoRepository;
import com.agogweasellane.interlocking.global.DefaultInterface;

@Service
public class EchoMongoService extends BaseMongoService<EchoMongoRepository, EchoDocument>
							  implements IhealthCheckable
{
	public EchoMongoService(EchoMongoRepository mRepository)
	{
		super(mRepository);
	}

	@Override
	public boolean isAvailable()
	{
		EchoDocument mongoDoc = null;
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-DD HH:mm:ss");
		formatter.withLocale( Locale.KOREA );

		clean();
		for(int idx=0; idx<5; idx++)
		{
			mongoDoc = new EchoDocument();
			mongoDoc.setSKey("test_" + Integer.toString(idx));
			mongoDoc.setDate( now.format(formatter) );
			mongoDoc.setTtl( System.currentTimeMillis()/1000L + 1000000L );
			insert(mongoDoc, true);
		}
		if(count()==0)		{	return false;	}

		return true;
	}
}