package com.github.defaultcore.mapper;

import com.github.defaultcore.domain.entity.Config;
import com.github.mybatis.crud.mapper.BatchInsertMapper;
import com.github.mybatis.crud.mapper.DefaultMapper;

/**
 *  系统配置 mapper
 *
 * @author wangpeiyuan
 * @date 2021-04-18 09:13:24
 */
public interface ConfigMapper extends DefaultMapper<Config>, BatchInsertMapper<Config> {
}