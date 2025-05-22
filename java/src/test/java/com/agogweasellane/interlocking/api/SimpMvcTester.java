package com.agogweasellane.interlocking.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.agogweasellane.interlocking.base.ApiMethodEnum;
import com.agogweasellane.interlocking.controllers.api.simple.dto.SimpleRequest;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class SimpMvcTester extends BaseMvcTester
{
	private SimpleRequest mRequest;

//	@Order(OrderInterface.init)
//	@Test
//	public void init()
//	{
//		if(repo.count()>0)	{	repo.deleteAll();		}
//	 }

	@Test
	@Override
	public void doTest() throws Exception
	{
		mUrl = "/simple";
		testGet();
		testPost();
		testPatch();
		testPut();
		testDelete();
	}
	@Override
	public void testRepository()
     {

     }
	@Override
	public void testService()
     {
		

		log.info( "TestService.FIN" );
     }

 	 @Override
 	 public void testGet() throws Exception
	 {
		MvcResult mvcResult = null;
		SimpleRequest request = new SimpleRequest();
		String responseBosy = null;

		mvcResult = mvc.perform(MockMvcRequestBuilders.get(mUrl)
						.accept(MediaType.TEXT_PLAIN))
						.andDo(print()).andExpect(status().isOk()).andReturn();
		responseBosy = mvcResult.getResponse().getContentAsString();
		assertThat( responseBosy.toLowerCase() ).contains( "simple get api" );

		log.info( "TestController.FIN" );
     }

	@Override
	public void testPost() throws Exception
	{
		SimpleRequest mRequest = new SimpleRequest();
		mRequest.setMethod(ApiMethodEnum.POST);
		mMvcResult = mvc.perform(MockMvcRequestBuilders.post(mUrl)
						.contentType(MediaType.APPLICATION_JSON)
		                .content(mRequest.doSeserialize())
						.accept(MediaType.APPLICATION_JSON))
				      	.andDo(print()).andExpect(status().isOk()).andReturn();
		responseString = mMvcResult.getResponse().getContentAsString();
		assertThat( responseString.toLowerCase() ).contains( "resultstatus" );
		assertThat( responseString ).contains( org.springframework.http.HttpStatus.OK.name() );
		
		mRequest.setMethod(null);
		mMvcResult = mvc.perform(MockMvcRequestBuilders.post(mUrl)
						.contentType(MediaType.APPLICATION_JSON)
		                .content(mRequest.doSeserialize())
						.accept(MediaType.APPLICATION_JSON))
				      	.andDo(print())
				      	.andExpect(status().isForbidden()).andReturn();
		responseString = mMvcResult.getResponse().getContentAsString();
		assertThat( responseString.toLowerCase() ).contains( "resultstatus" );
		assertThat( responseString ).contains( org.springframework.http.HttpStatus.FORBIDDEN.name() );
	}

	@Override
	public void testPut() throws Exception
	{
	}

	@Override
	public void testPatch() throws Exception
	{
		SimpleRequest mRequest = new SimpleRequest();
		mRequest.setMethod(ApiMethodEnum.PUT);
		mMvcResult = mvc.perform(MockMvcRequestBuilders.put(mUrl)
			      		.accept(MediaType.APPLICATION_JSON)
			      		.content(mRequest.doSeserialize())
						.accept(MediaType.TEXT_PLAIN))
				        .andDo(print())
				        .andExpect(status().isOk()).andReturn();
		responseString = mMvcResult.getResponse().getContentAsString();
		assertThat( responseString.toLowerCase() ).contains( "simple put api" );
	}

	@Override
	public void testDelete() throws Exception
	{
		SimpleRequest mRequest = new SimpleRequest();
		mRequest.setMethod(ApiMethodEnum.DELETE);
		mMvcResult = mvc.perform(MockMvcRequestBuilders.delete(mUrl)
						.accept(MediaType.APPLICATION_JSON)
			      		.content(mRequest.doSeserialize())
						.accept(MediaType.TEXT_PLAIN))
						.andDo(print())
						.andExpect(status().isOk()).andReturn();
		responseString = mMvcResult.getResponse().getContentAsString();
		assertThat( responseString.toLowerCase() ).contains( "simple delete api" );
	}
}