package com.agogweasellane.interlocking.models.service_layer.echo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agogweasellane.interlocking.base.IhealthCheckable;
import com.agogweasellane.interlocking.controllers.api.echo.dto.EchoDto;
import com.agogweasellane.interlocking.database.echo.EchoEntity;
import com.agogweasellane.interlocking.database.echo.EchoRepository;
import com.agogweasellane.interlocking.global.DefaultInterface;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EchoMariaService extends com.agogweasellane.interlocking.base.service.BaseService<EchoDto>
							  implements IhealthCheckable
{
	private final EchoRepository echoRepo;
	@Autowired
	public EchoMariaService(EchoRepository arg)
	{//CASE. 생성자 주입
		this.echoRepo=arg;
	}
	


	@Override
	public long count()
	{
		long result = DefaultInterface.DEFAULT_L;
		
		try {
			result = echoRepo.count();
		} catch (Throwable e) {
			log.error(e.getMessage());
		}
		
		return result;
	}
	
	
	@Override
	public boolean insert(boolean isOverWrite, EchoDto... argDto) {
		return isOverWrite;
		
	}

	@Override
	public boolean update(EchoDto... argDto)
	{
		boolean result = false;

		if(argDto.length==1)
		{
			EchoEntity entity = EchoEntity.builder()
								.host_v4(argDto[0].getHost_v4())
								.build();
			EchoEntity repoResult = echoRepo.save(entity);

			if(repoResult.getCreate_date()!=null)
			{
				log.debug(repoResult.getCreate_date().toString());
				result = true;
			}
			else if(repoResult.getEdit_date()!=null)
			{
				log.debug(repoResult.getEdit_date().toString());
				result = true;
			}
		}
		else
		{
			List<EchoEntity> list = new ArrayList<EchoEntity>();
			for(EchoDto tmpDto : argDto)
			{
				list.add( EchoEntity.builder().host_v4(tmpDto.getHost_v4()).build() );
			}
			echoRepo.saveAll(list);
			if(echoRepo.count()>=list.size()) {	result = true;	}
		}
		
		return result;
	}

	@Override
	public EchoDto findItem(EchoDto argDto) {
		return null;
	}

	@Override
	public List<EchoDto> findItmes(EchoDto argDto) {
		return null;
	}

	@Override
	public boolean update(String argQuery) {
		return false;
	}



	@Override
	public boolean isAvailable()
	{
		if(count()==DefaultInterface.DEFAULT_L)		{	return false;	}

		return true;
	}



	@Override
	public boolean delete(EchoDto... argDto) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'delete'");
	}
}