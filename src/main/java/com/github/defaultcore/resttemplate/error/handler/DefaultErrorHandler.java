package com.github.defaultcore.resttemplate.error.handler;

import com.github.defaultcore.resttemplate.error.exception.InvokeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
        String status = clientHttpResponse.getStatusCode().toString();
        String body = IOUtils.toString(clientHttpResponse.getBody(), StandardCharsets.UTF_8);

        throw new InvokeException("接口调用失败。返回内容 -> " + status + "，" + body);
    }
}
