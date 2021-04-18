package com.github.defaultcore.domain.entity;

import com.github.mybatis.crud.annotation.Id;
import com.github.mybatis.crud.annotation.Table;
import com.github.mybatis.crud.enums.CustomFillIdMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *  系统配置
 *
 * @author wangpeiyuan
 * @date 2021-04-18 09:13:24
 */
@Table(name = "CONFIG")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Config {

    /**
      * 
      */
    @Id(fillMethod = CustomFillIdMethod.UUID)
    private String id;
    /**
      * 代码
      */
    private String code;
    /**
      * 描述
      */
    private String description;
    /**
      * 是否启用
      */
    private Boolean isEnable;
}