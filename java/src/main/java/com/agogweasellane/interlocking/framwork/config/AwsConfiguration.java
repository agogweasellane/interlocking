package com.agogweasellane.interlocking.framwork.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AwsConfiguration 
{
    @Value("${custom.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${custom.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${custom.aws.region}")
    private String region;

    @Bean
    public AmazonS3Client amazonS3Client() {
        AWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                                                    .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                                                    .withRegion(region)
                                                    .build();
    }
}
