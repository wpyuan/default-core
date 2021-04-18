package com.github.defaultcore.service;


import com.github.defaultcore.domain.entity.Property;

import java.util.List;

/**
 *  属性值 service
 *
 * @author wangpeiyuan
 * @date 2021-04-13 15:33:21
 */
public interface PropertyService extends DefaultService<Property> {
    /**
     * 根据配置编码和属性值获取属性描述（只取启用的）
     * @param code 配置编码
     * @param propertyValue 属性值
     * @return 对应的属性描述
     */
    String getPropertyDesc(String code, String propertyValue);

    /**
     * 根据配置编码和属性值获取属性（只取启用的）
     * @param code 配置编码
     * @param propertyValue 属性值
     * @return 对应的属性
     */
    Property getProperty(String code, String propertyValue);

    /**
     * 根据配置编码获取属性集合（只取启用的）
     * @param code 配置编码
     * @return 对应的属性
     */
    List<Property> getProperties(String code);
}