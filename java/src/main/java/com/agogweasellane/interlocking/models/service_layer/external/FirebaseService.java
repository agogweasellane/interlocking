package com.agogweasellane.interlocking.models.service_layer.external;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.agogweasellane.interlocking.base.BaseDto;
import com.agogweasellane.interlocking.base.IhealthCheckable;
import com.agogweasellane.interlocking.base.service.BaseService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FirebaseService extends BaseService
							 implements IhealthCheckable
{
	@PostConstruct
	@Override
	public void doStartWithBooting()
	{
		log.info( "doStartWithBooting" );
		try
		{
			FileInputStream refreshToken = new FileInputStream("google/firebase-adminsdk.json");
			FirebaseOptions options = FirebaseOptions.builder()
															    .setCredentials(GoogleCredentials.fromStream(refreshToken))
															    .build();
			FirebaseApp.initializeApp(options);
		} catch (IOException e){
			log.error( "" );
		}
		
		
	}

	@Override
	public boolean insert(boolean isOverWrite, BaseDto... argDto) {
		return false;
	}

	@Override
	public boolean update(BaseDto... argDto) {
		return false;
	}

	@Override
	public boolean update(String argQuery) {
		return false;
	}

	@Override
	public BaseDto findItem(BaseDto argDto) {
		return null;
	}

	@Override
	public List findItmes(BaseDto argDto) {
		return null;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'isAvailable'");
	}

	@Override
	public boolean delete(BaseDto... argDto) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'delete'");
	}
}