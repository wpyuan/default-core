package com.github.defaultcore.domain.entity;

import com.github.mybatis.crud.annotation.Id;
import com.github.mybatis.crud.annotation.Table;
import com.github.mybatis.crud.enums.CustomFillIdMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *  属性值
 *
 * @author wangpeiyuan
 * @date 2021-04-18 09:16:07
 */
@Table(name = "PROPERTY")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    /**
      * 
      */
    @Id(fillMethod = CustomFillIdMethod.UUID)
    private String id;
    /**
      * 所属系统配置ID，取自CONFIG表.ID
      */
    private String configId;
    /**
      * 值
      */
    private String value;
    /**
      * 描述
      */
    private String description;
    /**
      * 排序号
      */
    private Long orderSeq;
    /**
      * 备注
      */
    private String remark;
    /**
      * 是否启用
      */
    private Boolean isEnable;
}