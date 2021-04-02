package com.github.defaultcore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * RestTemplate 配置类
 * </p>
 *
 * @author wpyuan 2020/04/15 14:53
 */
@Configuration
@ConfigurationProperties(prefix = "restTemplate")
public class RestTemplateProperty {
    private static final int DEFAULT_CONNECT_TIMEOUT = 60000;
    private static final int DEFAULT_READ_TIMEOUT = 60000;

    private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private int readTimeout = DEFAULT_READ_TIMEOUT;


    public RestTemplateProperty setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public RestTemplateProperty setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

}