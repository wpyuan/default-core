package com.github.defaultcore.util;

import com.github.defaultcore.helper.ApplicationContextHelper;
import lombok.experimental.UtilityClass;

/**
 * <p>
 *     处理器工具类
 * </p>
 *
 * @author wangpeiyuan
 * @date 2022/2/15 10:46
 */
@UtilityClass
public class HandlerUtil {

    /**
     * 获取处理器，优先从获取spring bean，如没有则是单纯的对象，不含注入bean
     *
     * @param handleClazz 处理器class
     * @return 处理器
     */
    public <T> T get(Class<? extends T> handleClazz) {
        T handler = ApplicationContextHelper.getBean(handleClazz);
        if (handler != null) {
            return handler;
        }
        return ReflectUtil.instance(handleClazz);
    }
}
