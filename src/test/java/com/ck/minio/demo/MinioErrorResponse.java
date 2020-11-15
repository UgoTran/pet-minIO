package com.ck.minio.demo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Error")
@XmlAccessorType(XmlAccessType.PROPERTY)
@ToString
@Getter
@Setter
public class MinioErrorResponse {
    @XmlElement
    private String Code;
    @XmlElement
    private String Message;
    @XmlElement
    private String Key;
    @XmlElement
    private String BucketName;
//    @XmlElement
//    private String Resource;
//    @XmlElement
//    private String RequestId;
//    @XmlElement
//    private String HostId;


    public MinioErrorResponse() {
    }

    public MinioErrorResponse(String code, String key, String bucketName) {
        Code = code;
        Key = key;
        BucketName = bucketName;
    }

}
