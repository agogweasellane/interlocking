package com.agogweasellane.interlocking.models.service_layer.external;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.agogweasellane.interlocking.base.service.BaseService;
import com.agogweasellane.interlocking.global.DefaultInterface;
import com.agogweasellane.interlocking.models.service_layer.dao.FileGoogleDto;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoogleCloudService extends BaseService<FileGoogleDto>
{
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
	
    @Value("${custom.google.drive.app.name}")	private String APP_NAME;
	@Value("${custom.google.secret.key.path}")	private String SECRET_PATH;
	@Value("${custom.google.tokens.path}")		private String TOKEN_PATH;

    private Credential mCredential;
    private Drive mDrive;
    


	
	@PostConstruct
	@Override
	public void doStartWithBooting() throws Exception
	{
		// InputStream ins = this.getClass().getResourceAsStream(SECRET_PATH);
		// if (ins == null) {   //https://stackoverflow.com/questions/32278204/specify-files-in-resources-folder-in-spring-application-properties-file
        //     throw new FileNotFoundException("Resource not found: " + SECRET_PATH);
        // }

		// NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		// GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(ins));
        // GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        //         HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        //         .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKEN_PATH)))
        //         .setAccessType("offline")
        //         .build();
        // LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        // mCredential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        // mDrive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, mCredential)
        //         .setApplicationName(APP_NAME)
        //         .build();
	}

	@Override
	public long count()
	{
		long result = DefaultInterface.DEFAULT_L;
		
		try {
			Set<String> keySet = mDrive.files().list().keySet();
			FileList fileList = mDrive.files().list().execute();
			List<File> files = fileList.getFiles();
			
			result = fileList.size();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return result;
	}

	@Override
	public boolean insert(boolean isOverWrite, FileGoogleDto... argDto) {
		return false;
	}

	@Override
	public boolean update(FileGoogleDto... argDto) {
		return false;
	}

	@Override
	public boolean update(String argQuery) {
		return false;
	}

	@Override
	public FileGoogleDto findItem(FileGoogleDto argDto) {
		return null;
	}

	@Override
	public List<FileGoogleDto> findItmes(FileGoogleDto argDto) {
		return null;
	}

	@Override
	public boolean delete(FileGoogleDto... argDto) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'delete'");
	}
}
/*
GoogleDrive와 Java 연동
	https://github.com/SangNyungLee/GoogleDrive-API-Java/tree/main
	https://velog.io/@poooq/Java-구글드라이브API를-사용해-자바랑-연결하고-CRUD작업까지-1
	https://velog.io/@poooq/Java-구글드라이브API를-사용해-자바랑-연결하고-CRUD작업까지-2

2021
    https://jsonobject.tistory.com/561
2023
    https://padosol.tistory.com/66
    https://wiki.yowu.dev/ko/Knowledge-base/Spring-Boot/Learning/032-integrating-with-a-file-storage-service-using-spring-boot-s3-google-drive
    https://medium.com/@logeshbommannan/uploading-files-to-google-drive-using-spring-boot-a-step-by-step-guide-2023-by-logesh-b-9b191b6c1b27
    
기타
	https://github.com/carbonrider/tutorial_google_drive_api_spring_boot
    
official.
	https://cloud.google.com/storage/docs/creating-buckets?hl=ko#storage-create-bucket-java
	https://github.com/GoogleCloudPlatform/spring-cloud-gcp/tree/main/spring-cloud-gcp-samples
	https://github.com/googleapis/google-api-java-client/
*/