package com.github.defaultcore.security.config;

import com.github.defaultcore.jwt.filter.JwtFilter;
import com.github.defaultcore.jwt.handler.JwtAccessDeniedHandler;
import com.github.defaultcore.jwt.handler.JwtAuthenticationEntryPoint;
import com.github.defaultcore.security.service.DefaultUserDetailsService;
import com.github.defaultcore.security.service.IHttpSecurityConfigure;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <p>
 * 默认security配置和密码加密认证处理器
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/4/22 8:35
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class DefaultWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    private final DefaultUserDetailsService defaultUserDetailsService;
    private final JwtFilter jwtFilter;
    private final IHttpSecurityConfigure iHttpSecurityConfigure;

    @Override
    protected UserDetailsService userDetailsService() {
        return defaultUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler());
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        iHttpSecurityConfigure.configure(http);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}