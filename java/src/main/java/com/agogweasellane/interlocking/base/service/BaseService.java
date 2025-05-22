package com.agogweasellane.interlocking.base.service;

import java.io.IOException;
import java.util.List;

import com.agogweasellane.interlocking.base.BaseDto;

public abstract class BaseService<Dto extends BaseDto>
{
	abstract public long count();
	abstract public boolean insert(boolean isOverWrite, Dto ...argDto);
	abstract public boolean update(Dto ...argDto);
	abstract public boolean update(String argQuery);
	abstract public Dto findItem(Dto argDto);
	abstract public List<Dto> findItmes(Dto argDto);
	abstract public boolean delete(Dto ...argDto);


	protected void doStartWithBooting() throws Exception
	{
/*
	@PostConstruct
	@Override
	public void doStartWithBooting()
	{
		... ... ...
*/

	}
}