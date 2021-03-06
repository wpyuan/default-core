package com.github.defaultcore.resttemplate.interceptor;

import com.github.defaultcore.aop.handler.IApiLogDataHandler;
import com.github.defaultcore.helper.ApplicationContextHelper;
import com.github.defaultcore.pojo.ApiLogData;
import com.github.defaultcore.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 接口请求调用拦截器
 * </p>
 *
 * @author wpyuan 2020/04/16 9:21
 */
@Slf4j
@Component
public class DefaultClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    public static final ThreadLocal<Map<String, Object>> MAP_THREAD_LOCAL = new ThreadLocal<>();
    public static final String FIELD_HANDLER = "handler";
    public static final String FIELD_ENCODING = "encoding";
    public static final String FIELD_BODY_MAX_LENGTH = "bodyMaxLength";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] bytes,
                                        ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        if (log.isTraceEnabled()) {
            log.trace("=========>>>> start 接口请求>>>> {} \"{}\", headers: {}, bytes: {}", request.getMethod(),
                    request.getURI(), request.getHeaders(), new String(bytes));
        }
        String ip = null;
        String contentType = null;
        try {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
            contentType = Objects.requireNonNull(request.getHeaders().getContentType()).toString();
        } catch (Exception e) {
            ;
        }
        String body = new String(bytes);
        ApiLogData apiLogData = ApiLogData.builder()
                .apiCode(StringUtils.defaultIfBlank((String) MAP_THREAD_LOCAL.get().get(ApiLogData.FIELD_API_CODE), "缺省"))
                .apiDesc(StringUtils.defaultIfBlank((String) MAP_THREAD_LOCAL.get().get(ApiLogData.FIELD_API_DESC), "缺省"))
                .url(String.valueOf(request.getURI()))
                .method(String.valueOf(request.getMethod()))
                .ip(ip)
                .requestHeaders(request.getHeaders().toString())
                .requestBody(MAP_THREAD_LOCAL.get().get(FIELD_BODY_MAX_LENGTH) == null ? body : body.substring(0, (Integer) MAP_THREAD_LOCAL.get().get(FIELD_BODY_MAX_LENGTH)))
                .requestContentType(contentType)
                .isInner(false)
                .requestDate(new Date())
                .build();

        Long startTime = System.currentTimeMillis();
        ClientHttpResponse response = null;
        Long endTime = 0L;
        try {
            response = clientHttpRequestExecution.execute(request, bytes);
            String responseContent = IOUtils.toString(response.getBody(), (Charset) ObjectUtils.defaultIfNull(MAP_THREAD_LOCAL.get().get(FIELD_ENCODING), StandardCharsets.UTF_8));
            if (log.isTraceEnabled()) {
                log.trace("=========<<<< end 接口请求<<<< 耗时: {}ms {}/{}, {} \"{}\" 返回body: {}", (endTime - startTime),
                        response.getStatusCode(), response.getStatusText(), request.getMethod(),
                        request.getURI(), responseContent);
            }
            apiLogData = apiLogData.toBuilder()
                    .isSuccess(response.getStatusCode().is2xxSuccessful())
                    .responseContent(responseContent)
                    .responseCode(response.getStatusCode() + "/" + response.getStatusText())
                    .build();
        } catch (Exception e) {
            apiLogData = apiLogData.toBuilder()
                    .isSuccess(false)
                    .exceptionStack(StringUtils.join(ExceptionUtils.getRootCauseStackTrace(e), StringUtils.LF))
                    .build();
            throw e;
        } finally {
            try {
                apiLogData = apiLogData.toBuilder()
                        .consumeTime(System.currentTimeMillis() - startTime)
                        .build();
                IApiLogDataHandler handler = this.getHandler((Class<? extends IApiLogDataHandler>) MAP_THREAD_LOCAL.get().get(FIELD_HANDLER));
                handler.handle(apiLogData);
            } catch (Exception e) {
                log.warn("接口日志记录异常", e);
            } finally {
                MAP_THREAD_LOCAL.remove();
            }
        }

        return response;
    }

    /**
     * 获取处理器，优先从获取spring bean，如没有则是单纯的对象，不含注入bean
     * @param handleClazz 处理器class
     * @return 处理器
     */
    public IApiLogDataHandler getHandler(Class<? extends IApiLogDataHandler> handleClazz) {
        try {
            return ApplicationContextHelper.getBean(handleClazz);
        } catch (NoSuchBeanDefinitionException e) {
            return ReflectUtil.instance(handleClazz);
        }
    }
}
