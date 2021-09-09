package com.github.defaultcore.aop;

import com.github.mybatis.crud.annotation.SortOrder;
import com.github.mybatis.crud.annotation.SortOrders;
import com.github.mybatis.crud.helper.SortHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 数据库查询排序aop
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/9 8:14
 */
@Aspect
@Component
@Slf4j
public class SortAspect {

    /**
     * 前置处理
     *
     * @param point 切点
     */
    @Before(value = "@annotation(sortOrder)")
    public void doBefore(JoinPoint point, SortOrder sortOrder) {
        SortHelper.set(sortOrder.sort(), sortOrder.alias());
    }

    /**
     * final处理
     *
     * @param point 切点
     */
    @After(value = "@annotation(sortOrder)")
    public void doAfter(JoinPoint point, SortOrder sortOrder) {
        SortHelper.clear();
    }

    /**
     * 前置处理
     *
     * @param point 切点
     */
    @Before(value = "@annotation(sortOrders)")
    public void doBefore2(JoinPoint point, SortOrders sortOrders) {
        for (SortOrder sortOrder : sortOrders.value()) {
            SortHelper.set(sortOrder.sort(), sortOrder.alias());
        }
    }

    /**
     * final处理
     *
     * @param point 切点
     */
    @After(value = "@annotation(sortOrders)")
    public void doAfter2(JoinPoint point, SortOrders sortOrders) {
        SortHelper.clear();
    }
}

