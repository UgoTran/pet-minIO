package com.ck.minio.demo;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;

@SpringBootTest
class DemoApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplicationTests.class);

    @Test
    void contextLoads() {
    }

    static OkHttpClient client = new OkHttpClient();

    @Test
    void checkLink() throws IOException, JAXBException {
//        RestTemplate restTemplate = new RestTemplate();

//        ResponseEntity<Object> response = new RestTemplate().getForEntity("http://192.168.1.3:9000/my-abq/unnamed.jpg", Object.class);
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.1.3:9000/my-abq/unname.jpg1").newBuilder();
        Request request = new Request.Builder().url(urlBuilder.build().toString()).build();

        try (Response response = client.newCall(request).execute()) {
            LOGGER.info("Content type: " + response.headers().get("Content-Type"));
            LOGGER.info("HttpStatus: " + HttpStatus.valueOf(response.code()));
            String result = response.body().string();
            LOGGER.info("response body: " + result);


            String test = "<Error><Code>NoSuchKey</Code><Message>The specified key does not exist.</Message><Key>unname.jpg1</Key><BucketName>my-abq</BucketName><Resource>/my-abq/unname.jpg1</Resource><RequestId>1647B7C3849548DA</RequestId><HostId>57f1fd7d-17ba-4a59-a09c-c1a3bf1f90e0</HostId></Error>";
            JAXBContext jaxbContext = JAXBContext.newInstance(MinioErrorResponse.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            MinioErrorResponse minioErrorResponse = (MinioErrorResponse) jaxbUnmarshaller.unmarshal(new StringReader(result));
            LOGGER.info("result convert: " + minioErrorResponse);
        }
    }
}
