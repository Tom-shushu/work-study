package com.zhouhong.util.documentutil.core.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(value = "aliyun.oss")
public class OssConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public OSS getOssClient(){
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
