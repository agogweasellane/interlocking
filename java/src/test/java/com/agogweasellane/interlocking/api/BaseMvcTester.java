package com.agogweasellane.interlocking.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.agogweasellane.interlocking.base.controller.BaseRequest;
import com.agogweasellane.interlocking.base.controller.BaseResponse;

@SuppressWarnings("rawtypes")
public abstract class BaseMvcTester<I extends BaseRequest, O extends BaseResponse>
{
	public abstract void doTest() throws Exception;
	public abstract void testGet() throws Exception;
	public abstract void testPost() throws Exception;
	public abstract void testPut() throws Exception;
	public abstract void testPatch() throws Exception;
	public abstract void testDelete() throws Exception;
	public abstract void testRepository() throws Exception;
	public abstract void testService() throws Exception;

	@Autowired protected MockMvc mvc;
	
	protected String mUrl = "";
	protected MvcResult mMvcResult;
	protected String responseString = "";
	
	protected String currentTime()
	{
		String result = null;
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-DD HH:mm:ss");
		formatter.withLocale( Locale.KOREA );
		
		return now.format(formatter);
	}
}