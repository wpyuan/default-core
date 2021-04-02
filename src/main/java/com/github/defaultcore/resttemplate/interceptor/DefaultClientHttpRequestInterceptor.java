package com.github.defaultcore.resttemplate.interceptor;

import com.github.defaultcore.aop.handler.IApiLogDataHandler;
import com.github.defaultcore.helper.ApplicationContextHepler;
import com.github.defaultcore.pojo.ApiLogData;
import com.github.mybatis.crud.util.EntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetAddress;
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

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = Exception.class)
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
        ApiLogData apiLogData = ApiLogData.builder()
                .apiCode(StringUtils.defaultIfBlank((String) MAP_THREAD_LOCAL.get().get(ApiLogData.FIELD_API_CODE), "缺省"))
                .apiDesc(StringUtils.defaultIfBlank((String) MAP_THREAD_LOCAL.get().get(ApiLogData.FIELD_API_DESC), "缺省"))
                .url(String.valueOf(request.getURI()))
                .method(String.valueOf(request.getMethod()))
                .ip(ip)
                .requestHeaders(request.getHeaders().toString())
                .requestBody(new String(bytes))
                .requestContentType(contentType)
                .isInner(false)
                .requestDate(new Date())
                .build();

        Long startTime = System.currentTimeMillis();
        ClientHttpResponse response = null;
        Long endTime = 0L;
        try {
            response = clientHttpRequestExecution.execute(request, bytes);
            String responesContent = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
            if (log.isTraceEnabled()) {
                log.trace("=========<<<< end 接口请求<<<< 耗时: {}ms {}/{}, {} \"{}\" 返回body: {}", (endTime - startTime),
                        response.getStatusCode(), response.getStatusText(), request.getMethod(),
                        request.getURI(), responesContent);
            }
            apiLogData = apiLogData.toBuilder()
                    .isSuccess(response.getStatusCode().is2xxSuccessful())
                    .responseContent(responesContent)
                    .responseCode(response.getStatusCode() + "/" + response.getStatusText())
                    .build();
        } catch (Exception e) {
            apiLogData = apiLogData.toBuilder()
                    .isSuccess(false)
                    .exceptionStack(StringUtils.join(ExceptionUtils.getRootCauseStackTrace(e), StringUtils.LF))
                    .build();
            throw e;
        } finally {
            apiLogData = apiLogData.toBuilder()
                    .consumeTime(System.currentTimeMillis() - startTime)
                    .build();
            IApiLogDataHandler handler = this.getHandler((Class<? extends IApiLogDataHandler>) MAP_THREAD_LOCAL.get().get(FIELD_HANDLER));
            handler.handle(apiLogData);
            MAP_THREAD_LOCAL.remove();
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
            return ApplicationContextHepler.getBean(handleClazz);
        } catch (NoSuchBeanDefinitionException e) {
            return EntityUtil.instance(handleClazz);
        }
    }
}
