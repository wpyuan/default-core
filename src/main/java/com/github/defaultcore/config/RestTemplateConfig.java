package com.github.defaultcore.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.github.defaultcore.resttemplate.error.handler.DefaultErrorHandler;
import com.github.defaultcore.resttemplate.interceptor.DefaultClientHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;



/**
 * <p>
 * RestTemplate 配置类
 * </p>
 *
 * @author wpyuan 2020/04/15 14:53
 */
@Configuration
@DependsOn({"defaultClientHttpRequestInterceptor"})
@EnableConfigurationProperties(RestTemplateProperty.class)
public class RestTemplateConfig {

    @Autowired
    private DefaultClientHttpRequestInterceptor defaultClientHttpRequestInterceptor;

    /**
     * 基于OkHttp3配置RestTemplate
     * @return RestTemplate okHttp客户端
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateProperty restTemplateProperty) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        fastJsonHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8")));
        RestTemplate restTemplate = new RestTemplateBuilder()
                .errorHandler(new DefaultErrorHandler())
                .interceptors(defaultClientHttpRequestInterceptor)
                .setConnectTimeout(Duration.ofMillis(restTemplateProperty.getConnectTimeout()))
                .setReadTimeout(Duration.ofMillis(restTemplateProperty.getReadTimeout()))
                .messageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8), fastJsonHttpMessageConverter)
                .build();
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new OkHttp3ClientHttpRequestFactory()));

        return restTemplate;
    }

}

