package com.example.kawasakirestapi.domain.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.example.kawasakirestapi.domain.setting.AwsS3Setting;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * AWS S3の設定
 *
 * @author kawasakiryosuke
 */
@Configuration
@RequiredArgsConstructor
public class AwsS3Config {
    private final AwsS3Setting awsS3Setting;
    /**
     * s3アクセスに必要な情報
     *
     * @return
     */
    @Bean
    public AmazonS3 getAwsS3() {
        AWSCredentials credentials =
                new BasicAWSCredentials(awsS3Setting.getAccessKey(), awsS3Setting.getSecretKey());

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(awsS3Setting.getRegion())
                .build();
    }
}
