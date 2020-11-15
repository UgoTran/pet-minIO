package com.ck.minio.demo;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

@Configuration
public class MinioConfig {

    private final Logger LOGGER = Logger.getLogger("MinioConfig");
    //bucket has naming: https://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html
    public static final String myBucket = "my-abq";

    @Bean
    public MinioClient buildMinio() throws IOException, InvalidKeyException, InvalidResponseException,
            InsufficientDataException, NoSuchAlgorithmException, ServerException,
            InternalException, XmlParserException, ErrorResponseException, BucketPolicyTooLargeException, InvalidBucketNameException, RegionConflictException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://192.168.1.3:9000")
                .credentials("minioadmin", "minioadmin")
//                .endpoint("https://mbal-minio-server.ntq.solutions")
//                .credentials("mbal", "mbal@12345")
                .build();
        LOGGER.info("done build minio");

        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(myBucket).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(myBucket).build());
            LOGGER.info("done build bucket" + myBucket);
        }
        LOGGER.info("====== bucket info:");
//        SseConfiguration sseConfiguration = minioClient.getBucketEncryption(GetBucketEncryptionArgs.builder().bucket(myBucket).build());
//        LOGGER.info("getBucketEncryption:" + sseConfiguration);
//
//        LifecycleConfiguration lifecycleConfiguration = minioClient.getBucketLifecycle(GetBucketLifecycleArgs.builder().bucket(myBucket).build());
//        LOGGER.info("getBucketLifecycle:" + lifecycleConfiguration);
//
//        NotificationConfiguration notificationConfiguration = minioClient.getBucketNotification(GetBucketNotificationArgs.builder().bucket(myBucket).build());
//        LOGGER.info("getBucketNotification:" + notificationConfiguration);
//
        String bucketPolicy = minioClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket(myBucket).build());
        LOGGER.info("getBucketPolicy:" + bucketPolicy);
//
//        ReplicationConfiguration replicationConfiguration = minioClient.getBucketReplication(GetBucketReplicationArgs.builder().bucket(myBucket).build());
//        LOGGER.info("getBucketReplication:" + replicationConfiguration);
//
        Tags tags = minioClient.getBucketTags(GetBucketTagsArgs.builder().bucket(myBucket).build());
        LOGGER.info("getBucketTags:" + tags.get());
//
//        VersioningConfiguration versioningConfiguration = minioClient.getBucketVersioning(GetBucketVersioningArgs.builder().bucket(myBucket).build());
//        LOGGER.info("getBucketVersioning:" + versioningConfiguration);
//
//        ObjectLockConfiguration objectLockConfiguration = minioClient.getObjectLockConfiguration(GetObjectLockConfigurationArgs.builder().bucket(myBucket).build());
//        LOGGER.info("getObjectLockConfiguration:" + versioningConfiguration);
//        LOGGER.info("Mode: " + objectLockConfiguration.mode());
//        LOGGER.info("Duration: " + objectLockConfiguration.duration().duration() + " " + objectLockConfiguration.duration().unit());

        return minioClient;
    }
}
