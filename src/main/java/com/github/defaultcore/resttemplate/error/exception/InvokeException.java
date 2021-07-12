package com.github.defaultcore.resttemplate.error.exception;

/**
 * <p>
 * 接口调用异常
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/7/12 8:57
 */
public class InvokeException extends RuntimeException {
    public InvokeException() {
        super();
    }

    public InvokeException(String message) {
        super(message);
    }

    public InvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvokeException(Throwable cause) {
        super(cause);
    }
}
