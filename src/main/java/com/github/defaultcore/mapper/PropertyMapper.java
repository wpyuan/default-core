package com.github.defaultcore.mapper;

import com.github.defaultcore.domain.entity.Property;
import com.github.mybatis.crud.mapper.BatchInsertMapper;
import com.github.mybatis.crud.mapper.DefaultMapper;

/**
 *  属性值 mapper
 *
 * @author wangpeiyuan
 * @date 2021-04-18 09:16:07
 */
public interface PropertyMapper extends DefaultMapper<Property>, BatchInsertMapper<Property> {
}