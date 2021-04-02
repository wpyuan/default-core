package com.github.defaultcore.resttemplate.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * <p>
 * restTemlpate默认错误处理类
 * </p>
 *
 * @author peiyuan.wang@hand-china.com 2020/04/15 18:23
 */
@Slf4j
public class DefaultErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        if (clientHttpResponse.getStatusCode().is2xxSuccessful()) {
            return false;
        }
        return true;
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        throw new RuntimeException("调用失败");
    }
}
