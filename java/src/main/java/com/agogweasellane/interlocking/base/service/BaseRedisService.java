package com.agogweasellane.interlocking.base.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.agogweasellane.interlocking.base.database.BaseRedisEntity;
import com.agogweasellane.interlocking.global.DefaultInterface;

@Service
public abstract class BaseRedisService<T extends CrudRepository, O extends BaseRedisEntity>
{
/*
	가이드.
	DB와 달리 사용치 않는 경우도 있다보니,
	BaseRestController에서 기본 구성이 생성자를 통한 주입인것과 달리 '필드에서 의존성 주입'하는 방식으로 사용.
	
	예시.
public class EchoRestController extends BaseRestController<EchoRestRequest, EchoRestResponse, EchoService>
{
	@Autowired
	private EchoRedisService mEchoRedisService;
	... ... ...
	@Override
	public EchoRestResponse postControll(EchoRestRequest argRequest) throws Exception
	{
		... ... ...
		mEchoRedisService.insert(redisEntity, true);
*/
	
	protected final T mRepository;
	
	@Autowired
	public BaseRedisService(T mRepo)
	{
        this.mRepository = mRepo;
    }
	
	public boolean insert(O argEntity, boolean isOverWrite)
	{
		boolean result = false;
		
		if(isOverWrite==true)
		{
			mRepository.save(argEntity);
		}
		
		return result;
	}
	
	public boolean clean()
	{
		boolean result = false;
		
		mRepository.deleteAll();
		if(mRepository.count()<1)
		{
			result = true;
		}
		
		return result;
	}
	
	public long count()
	{
		long result = DefaultInterface.DEFAULT_L;
		
		result = mRepository.count();
		
		return result;
	}
	
	public O findById(String argId)
	{
		Optional<O> result = null;

		result = mRepository.findById(argId);
		if(result==null)
		{
			return null;
		}
		
		return result.get();
	}
	
	public O findById(long argId)
	{
		Optional<O> result = null;

		result = mRepository.findById(argId);
		if(result==null)
		{
			return null;
		}
		
		return result.get();
	}
}