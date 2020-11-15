package com.ck.minio.demo;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class UploadFile {
    Logger LOGGER = Logger.getLogger("UploadFile");

    @Autowired
    public MinioClient minio;

    void uploadStrategy(int strategy, MultipartFile file) {
        try {
            String objectUrl;
            switch (strategy) {
                case 1:
                    //ObjectWriteResponse return etag & versionId
                    LOGGER.info("upload One");
                    ObjectWriteResponse objectWriteResponse = minio.uploadObject(UploadObjectArgs.builder()
                            .bucket(MinioConfig.myBucket)
                            .object("mygirl.jpg")
                            .filename("/home/amourb/Pictures/twe.jpg")
                            .build());
                    LOGGER.info("upload mygirl done!");
                    LOGGER.info("OWR: " + objectWriteResponse.etag() + " | " + objectWriteResponse.versionId());
                    break;
                case 2:
                    LOGGER.info("composeObject");
                    break;
                case 3:
                    LOGGER.info("downloadObject(DownloadObjectArgs args)");
                    minio.downloadObject(
                            DownloadObjectArgs.builder()
                                    .bucket(MinioConfig.myBucket)
                                    .object("mygirl.jpg")
                                    .build());
                    break;
                case 4:
                    LOGGER.info("getObjectTags(GetObjectTagsArgs args)");
                    Tags tags = minio.getObjectTags(GetObjectTagsArgs.builder().bucket(MinioConfig.myBucket).object("mygirl.jpg").build());
                    //null - when not config
                    LOGGER.info("tags: " + tags.get().toString());
                    break;
                case 5:
                    LOGGER.info("getPresignedObjectUrl(GetPresignedObjectUrlArgs args)");
                    String presignedObjectUrl = minio.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                    //another method return url, but not get content file
                                    .method(Method.GET)
//                                    .bucket(MinioConfig.myBucket)
                                    .bucket("mbal-dev")
                                    .region("mbal-dev")
                                    /* wrong objet still return url, but hit url return error 404, content type
                                    HTTP/1.1 404 Not Found
                                    Accept-Ranges: bytes
                                    Content-Length: 313
                                    Content-Security-Policy: block-all-mixed-content
                                    Content-Type: application/xml
                                    Server: MinIO/RELEASE.2020-11-13T20-10-18Z
                                    Vary: Origin
                                    X-Amz-Request-Id: 164790693DBD190A
                                    X-Xss-Protection: 1; mode=block
                                    Date: Sun, 15 Nov 2020 03:22:53 GMT
                                    */
                                    //right to check url before return client
                                    .object("2020-11-10_12-20.jpeg")
                                    .build());
                    //runtime if minio die, still return url, url connect refused
                    /* url valid return this:
                    HTTP/1.1 200 OK
                    Accept-Ranges: bytes
                    Content-Length: 222768
                    Content-Security-Policy: block-all-mixed-content
                    Content-Type: image/jpeg
                    ETag: "48bdda5622a97d3c47089b95c1bb260a"
                    Last-Modified: Sat, 14 Nov 2020 16:45:01 GMT
                    Server: MinIO/RELEASE.2020-11-13T20-10-18Z
                    Vary: Origin
                    X-Amz-Request-Id: 1647907442CD91B1
                    X-Xss-Protection: 1; mode=block
                    Date: Sun, 15 Nov 2020 03:23:41 GMT
                     */
                    LOGGER.info("presigned url: " + presignedObjectUrl);
                    break;
                case 6:
                    LOGGER.info("getObjectUrl(String bucketName, String objectName)");
                    //if object policy public read access, url will work
                    // inf not return 404
                    objectUrl = minio.getObjectUrl(MinioConfig.myBucket, "unnamed.jpg");
                    LOGGER.info("DOWNLOAD URLL: " + objectUrl);
                    break;
                case 7:
                    LOGGER.info("statObject(StatObjectArgs args)");
                    // Get information of an object.
                    ObjectStat objectStat = minio.statObject(StatObjectArgs.builder().bucket(MinioConfig.myBucket).object("mygirl.jpg").build());
                    LOGGER.info("object information and metadata : " + objectStat);
                    break;
                case 8:
                    LOGGER.info("putObject(PutObjectArgs args)");
                    ObjectWriteResponse putRes = minio.putObject(
                            PutObjectArgs.builder()
                                    .bucket(MinioConfig.myBucket)
                                    .object(file.getOriginalFilename())
                                    .stream(file.getInputStream(), file.getSize(), -1)
                                    .contentType(file.getContentType())
                                    .build());
                    LOGGER.info("res: " + putRes.etag());
                    objectUrl = minio.getObjectUrl(MinioConfig.myBucket, file.getOriginalFilename());
                    LOGGER.info("url: " + objectUrl);
                    break;

            }

        } catch (Exception ex) {
            // throw error if not existfile: ... not a regular file
            // ERROR: The specified bucket does not exist
            // if connect fail/taime out throw exception
            LOGGER.severe("ERROR: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @PostMapping("/upload")
    public void uploadPhotos(@RequestPart("file") MultipartFile file) {
        uploadStrategy(8, file);
    }
}
