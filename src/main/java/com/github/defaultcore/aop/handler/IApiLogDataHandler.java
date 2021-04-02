package com.github.defaultcore.aop.handler;


import com.github.defaultcore.pojo.ApiLogData;

/**
 * api日志数据处理器
 * @author PeiYuan
 */
public interface IApiLogDataHandler {

    /**
     * 日志数据处理
     * @param apiLogData 日志数据
     */
    void handle(ApiLogData apiLogData);
}
