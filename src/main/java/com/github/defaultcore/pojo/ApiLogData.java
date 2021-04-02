package com.github.defaultcore.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *  接口日志数据
 *
 * @author wangpeiyuan
 * @date 2021-04-01 17:43:24
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ApiLogData {

    public static final String FIELD_API_CODE = "apiCode";
    public static final String FIELD_API_DESC = "apiDesc";
    /**
     * 接口代码
     */
    private String apiCode;
    /**
     * 接口描述
     */
    private String apiDesc;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求者IP地址
     */
    private String ip;
    /**
     * 请求头部信息
     */
    private String requestHeaders;
    /**
     * 请求参数Query部分
     */
    private String requestQuery;
    /**
     * 请求参数Body部分
     */
    private String requestBody;
    /**
     * 是否成功
     */
    private Boolean isSuccess;
    /**
     * 返回内容
     */
    private String responseContent;
    /**
     * 异常堆栈信息
     */
    private String exceptionStack;
    /**
     * 耗时
     */
    private Long consumeTime;
    /**
     * 是否系统内部接口，系统内部接口：true，外部接口：false
     */
    private Boolean isInner;
    /**
     * 请求的ContentType
     */
    private String requestContentType;
    /**
     * 请求时间
     */
    private Date requestDate;
    /**
     * 请求外部接口的返回状态码，当IS_INNER字段为false时有值
     */
    private String responseCode;
}
