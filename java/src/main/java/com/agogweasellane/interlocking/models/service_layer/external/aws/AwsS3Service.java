package com.agogweasellane.interlocking.models.service_layer.external.aws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import com.agogweasellane.interlocking.base.IhealthCheckable;
import com.agogweasellane.interlocking.base.service.BaseService;
import com.agogweasellane.interlocking.controllers.FileDto;
import com.agogweasellane.interlocking.global.DefaultInterface;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service extends BaseService<S3ObjectDto>
                          implements IhealthCheckable
{
/*
    파일명에 한글이 있으면... private버킷에서는 그냥 다운로드만 되는 이슈가 있음. 영문파일은 웹뷰가 되지만.
    정책JSON 편집시, IAMAccessAnalyzerFullAccess 권한필수.
*/
    private final AmazonS3Client mS3Client;

    @Override
    public boolean isAvailable()
    {
        List<Bucket> bList = mS3Client.listBuckets();
        if(bList.isEmpty() || bList.size()<S3BucketEnum.path_test.ordinal())
        {
            return false;
        }

        return true;
    }


    @Override
    public long count()
    {
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public boolean insert(boolean isOverWrite, S3ObjectDto... argDto) throws AmazonS3Exception
    {
        PutObjectRequest request = null;
        PutObjectResult result  = null;
        ObjectMetadata objMeta = null;
        boolean isExistedObject = true;
        StringBuilder filePath = new StringBuilder();
        
        for(S3ObjectDto tmpData : argDto)
        {
            try {
                filePath.setLength(0);
                filePath.append(tmpData.getPath()).append(tmpData.getFile().getFullName());

                isExistedObject = false;
                isExistedObject = mS3Client.doesObjectExist(tmpData.getRoot().getValue(), filePath.toString());

                if(isExistedObject==true && isOverWrite==false)
                {//CASE. 기존에 존재하는건 OW 안 함.
                    continue;
                }
            } catch (SdkClientException e) {
                log.debug("not exist={}", tmpData.getFile().getFullName());
            }

            objMeta = new ObjectMetadata();
            objMeta.setContentType( tmpData.getFile().getContentType() );
            objMeta.setContentLength( tmpData.getFile().getBytes().length );            
            request = new PutObjectRequest(tmpData.getRoot().getValue(), filePath.toString(), new ByteArrayInputStream(tmpData.getFile().getBytes()), objMeta);
            request.setMetadata(objMeta);

            if(tmpData.getRoot().getPermit() == DefaultInterface.PUBLIC_ALLOW)
            {
                request.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            else
            {
                request.withCannedAcl(CannedAccessControlList.AuthenticatedRead);
            }
            

            result = mS3Client.putObject(request);
            log.debug("insert. result={}", result.isRequesterCharged());
        }
        //mS3Client.getUrl(bucket, fileName).toString();

        return true;
    }

    @Override
    public boolean update(S3ObjectDto... argDto)
    {
        return insert(true, argDto);
    }

    @Override
    public boolean update(String argQuery)
    {
        return true;
    }

    @Override
    public S3ObjectDto findItem(S3ObjectDto argDto)
    {
        S3ObjectDto result = null;
        S3Object tmpS3Object = null;
        StringBuilder filePath = new StringBuilder();

        try {
            filePath.setLength(0);
            filePath.append(argDto.getPath()).append(argDto.getFile().getFullName());

            tmpS3Object = mS3Client.getObject(argDto.getRoot().getValue(), filePath.toString());
            result = S3ObjectDto.builder()
                                .root(argDto.getRoot())
                                .file( FileDto.builder().bytes(IOUtils.toByteArray(tmpS3Object.getObjectContent())).fullName(argDto.getFile().getFullName()).build() )
                                .build();
        } catch (AmazonS3Exception e) {
            log.debug("not exist={}", argDto.getFile().getFullName());
            tmpS3Object = null;
        } catch (IOException e) {
            log.debug("IOUtils error", e);
        }

        return result;
    }

    @Override
    public List findItmes(S3ObjectDto argDto)
    {//NOT-USE
        throw new UnsupportedOperationException("Unimplemented method 'findItmes'");
    }


    @Override
    public boolean delete(S3ObjectDto... argDto)
    {
        boolean result = true;
        S3Object tmpS3Object = null;
        StringBuilder filePath = new StringBuilder();
        boolean isExistedObject = true;

        for(S3ObjectDto tmpData : argDto)
        {
            filePath.setLength(0);
            filePath.append(tmpData.getPath()).append(tmpData.getFile().getFullName());

            mS3Client.deleteObject(tmpData.getRoot().getValue(), filePath.toString());

            isExistedObject = mS3Client.doesObjectExist(tmpData.getRoot().getValue(), filePath.toString());
            if(isExistedObject==true){  result = true; }
        }

        return result;
    }
}
