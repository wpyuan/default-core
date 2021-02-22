package com.github.defaultcore.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Administrator
 */
@Slf4j
@UtilityClass
public class ReflectUtil {

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.
     * 如public class EmployeeService extends DefaultService<Employee, EmployeeDTO>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     */
    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        try {
            if (!(params[index] instanceof Class)) {
                return Object.class;
            }
        } catch (IndexOutOfBoundsException e) {
            return Object.class;
        }

        return (Class) params[index];
    }
}
