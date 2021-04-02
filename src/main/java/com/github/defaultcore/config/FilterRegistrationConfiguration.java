package com.github.defaultcore.config;

import com.github.defaultcore.wrapper.ReuseHttpServletRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author PeiYuan
 */
@Configuration
public class FilterRegistrationConfiguration {
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(reuseRequestFilter());
        registration.addUrlPatterns("/*");
        registration.setName("reuseRequestFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public Filter reuseRequestFilter() {
        return new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                HttpServletRequest httpServletRequest = new ReuseHttpServletRequest((HttpServletRequest)request);
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                chain.doFilter(httpServletRequest,httpServletResponse);
            }
        };
    }

}
