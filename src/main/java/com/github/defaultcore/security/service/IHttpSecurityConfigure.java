package com.github.defaultcore.security.service;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * <p>
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/4/22 16:55
 */
public interface IHttpSecurityConfigure {
    /**
     * 扩展HttpSecurity配置
     *
     * @param httpSecurity
     * @throws Exception
     */
    void configure(HttpSecurity httpSecurity) throws Exception;
}
