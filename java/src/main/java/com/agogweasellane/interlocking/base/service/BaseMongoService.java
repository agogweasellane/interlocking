package com.agogweasellane.interlocking.base.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.agogweasellane.interlocking.base.database.BaseDocument;
import com.agogweasellane.interlocking.global.DefaultInterface;

public abstract class BaseMongoService<T extends MongoRepository, O extends BaseDocument>
{
	protected final T mRepository;
	
	@Autowired
	public BaseMongoService(T mRepository)
	{
        this.mRepository = mRepository;
    }
	
	public boolean insert(O argEntity, boolean isOverWrite)
	{
		boolean result = false;
		
		if(isOverWrite==true)
		{
			mRepository.save(argEntity);
		}
		else
		{
			if(mRepository.existsById(argEntity)==false)
			{
				mRepository.save(argEntity);
			}
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