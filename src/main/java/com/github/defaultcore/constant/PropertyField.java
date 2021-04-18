package com.github.defaultcore.constant;

/**
 *  属性值字段常量，供查询方法使用
 *
 * @author wangpeiyuan
 * @date 2021-04-18 09:16:07
 */
public interface PropertyField {

    /**
      * 
      */
    String ID = "id";
    /**
      * 所属系统配置ID，取自CONFIG表.ID
      */
    String CONFIG_ID = "configId";
    /**
      * 值
      */
    String VALUE = "value";
    /**
      * 描述
      */
    String DESCRIPTION = "description";
    /**
      * 排序号
      */
    String ORDER_SEQ = "orderSeq";
    /**
      * 备注
      */
    String REMARK = "remark";
    /**
      * 是否启用
      */
    String IS_ENABLE = "isEnable";

}