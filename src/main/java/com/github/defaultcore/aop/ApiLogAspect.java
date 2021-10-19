package com.github.defaultcore.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.defaultcore.annotation.EnableApiLog;
import com.github.defaultcore.aop.handler.IApiLogDataHandler;
import com.github.defaultcore.helper.ApplicationContextHelper;
import com.github.defaultcore.pojo.ApiLogData;
import com.github.defaultcore.util.ReflectUtil;
import com.github.defaultcore.wrapper.ReuseHttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author PeiYuan
 */
@Aspect
@Component
@Slf4j
public class ApiLogAspect {

    /**
     * 环绕
     *
     * @param proceedingJoinPoint
     * @return 结果
     * @throws Throwable
     */
    @Around(value = "@annotation(enableApiLog)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, EnableApiLog enableApiLog) throws Throwable {
        // 得到 HttpServletRequest
        HttpServletRequest request = new ReuseHttpServletRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());

        // 获取注解信息
        ApiLogData apiLogData = ApiLogData.builder()
                .apiCode(enableApiLog.code())
                .apiDesc(enableApiLog.desc())
                .url(request.getRequestURL().toString())
                .method(request.getMethod())
                .ip(request.getRemoteAddr())
                .requestHeaders(this.getHeaders(request))
                .requestQuery(this.getQuery(request))
                .requestBody(this.getBody(request, proceedingJoinPoint))
                .requestContentType(request.getContentType())
                .isInner(true)
                .requestDate(new Date())
                .build();
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
            apiLogData = apiLogData.toBuilder().isSuccess(true)
                    .responseContent(JSON.toJSONString(result))
                    .build();
        } catch (Throwable e) {
            apiLogData = apiLogData.toBuilder().isSuccess(false)
                    .exceptionStack(StringUtils.join(ExceptionUtils.getRootCauseStackTrace(e), StringUtils.LF))
                    .build();
            throw e;
        } finally {
            // 执行时间
            apiLogData = apiLogData.toBuilder().consumeTime(System.currentTimeMillis() - startTime).build();
            IApiLogDataHandler handler = this.getHandler(enableApiLog.handler());
            handler.handle(apiLogData);
        }

        return result;
    }

    private String getHeaders(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> headNames = request.getHeaderNames();
        while (headNames.hasMoreElements()) {
            String headName = headNames.nextElement();
            jsonObject.put(headName, request.getHeader(headName));
        }

        return jsonObject.toJSONString();
    }

    private String getQuery(HttpServletRequest request) {
        return request.getQueryString();
    }

    private String getBody(HttpServletRequest request, ProceedingJoinPoint proceedingJoinPoint) {
        String body = null;
        String contentType = request.getContentType();
        if (contentType == null) {
            return body;
        }
        if (contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE) || MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(contentType)) {
            // 以JSON存储
            CodeSignature codeSignature = (CodeSignature) proceedingJoinPoint.getSignature();
            Map<String, Object> map = new HashMap<>(1);
            Enumeration<String> paramMap = request.getParameterNames();
            String[] paramNames = codeSignature.getParameterNames();
            Class[] paramType = codeSignature.getParameterTypes();
            while (paramMap.hasMoreElements()) {
                String name = paramMap.nextElement();
                map.put(name, request.getParameter(name));
            }
            ConvertUtils.register(new Converter() {
                private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
                private static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";
                private static final String TIME_STAMP_FORMAT = "^\\d+$";
                @Override
                public <T> T convert(Class<T> type, Object beforeValue) {
                    if (!(beforeValue instanceof String)) {
                        return null;
                    }
                    String value = (String) beforeValue;
                    if(StringUtils.isEmpty(value)) {
                        return null;
                    }

                    value = value.trim();

                    try {
                        if (value.contains("-")) {
                            SimpleDateFormat formatter;
                            if (value.contains(":")) {
                                formatter = new SimpleDateFormat(DATE_FORMAT);
                            } else {
                                formatter = new SimpleDateFormat(SHORT_DATE_FORMAT);
                            }
                            return (T) formatter.parse(value);
                        } else if (value.matches(TIME_STAMP_FORMAT)) {
                            Long lDate = new Long(value);
                            return (T) new Date(lDate);
                        }
                    } catch (Exception e) {
                        return null;
                    }
                    return null;
                }
            }, Date.class);
            Object[] args = proceedingJoinPoint.getArgs();
            for (int i = 0; i < paramNames.length; i++) {
                if (map.containsKey(paramNames[i])) {
                    map.put(paramNames[i], ConvertUtils.convert(map.get(paramNames[i]), paramType[i]));
                } else if (MultipartFile.class.isAssignableFrom(args[i].getClass())) {
                    // 包含文件参数 MultipartFile，只记录文件名
                    map.put(paramNames[i], ((MultipartFile) args[i]).getOriginalFilename());
                }
            }
            body = JSON.toJSONString(map);
        } else {
            body = ((ReuseHttpServletRequest) request).getBody();
        }
        return body;
    }

    /**
     * 获取处理器，优先从获取spring bean，如没有则是单纯的对象，不含注入bean
     * @param handleClazz 处理器class
     * @return 处理器
     */
    public IApiLogDataHandler getHandler(Class<? extends IApiLogDataHandler> handleClazz) {
        IApiLogDataHandler apiLogDataHandler = ApplicationContextHelper.getBean(handleClazz);
        if (apiLogDataHandler != null) {
            return apiLogDataHandler;
        }
        return ReflectUtil.instance(handleClazz);
    }
}

