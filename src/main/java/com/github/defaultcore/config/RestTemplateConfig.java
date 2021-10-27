package com.github.defaultcore.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.github.defaultcore.resttemplate.error.handler.DefaultErrorHandler;
import com.github.defaultcore.resttemplate.interceptor.DefaultClientHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;


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
    @Bean("dcRestTemplate")
    public RestTemplate restTemplate(RestTemplateProperty restTemplateProperty) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        fastJsonHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        RestTemplate original = new RestTemplate();
        List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<>();
        httpMessageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        httpMessageConverters.add(fastJsonHttpMessageConverter);
        httpMessageConverters.add(new FormHttpMessageConverter());
        httpMessageConverters.addAll(original.getMessageConverters());
        RestTemplate restTemplate = new RestTemplateBuilder()
                .errorHandler(new DefaultErrorHandler())
                .interceptors(defaultClientHttpRequestInterceptor)
                .messageConverters(httpMessageConverters)
                .build();
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory();
        okHttp3ClientHttpRequestFactory.setConnectTimeout(restTemplateProperty.getConnectTimeout());
        okHttp3ClientHttpRequestFactory.setReadTimeout(restTemplateProperty.getReadTimeout());
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(okHttp3ClientHttpRequestFactory));

        return restTemplate;
    }

}

