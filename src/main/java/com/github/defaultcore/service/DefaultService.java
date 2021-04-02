package com.github.defaultcore.service;

import com.github.mybatis.crud.structure.Condition;
import com.github.mybatis.crud.structure.LeftJoin;

import java.util.List;

/**
 * service默认方法
 *
 * @author PeiYuan
 */
public interface DefaultService<E> {
    /**
     * 全字段插入
     *
     * @param entity 实体类
     * @return 是否插入成功
     */
    Boolean insert(E entity);

    /**
     * 有值的字段插入
     *
     * @param entity 实体类
     * @return 是否插入成功
     */
    Boolean insertSelective(E entity);

    /**
     * 全字段插入
     * @param list 实体集合
     */
    void batchInsert(List<E> list);

    /**
     * 有值的字段插入
     * @param list 实体集合
     */
    void batchInsertSelective(List<E> list);

    /**
     * 根据主键删除记录
     *
     * @param entity 实体类
     * @return 影响条数
     */
    int deleteByPrimaryKey(E entity);

    /**
     * 根据条件删除记录
     *
     * @param entity 实体类，有值的条件关系是等于
     * @return 影响条数
     */
    int delete(E entity);

    /**
     * 根据主键批量删除记录
     * @param list 需删除的记录
     * @return 影响条数
     */
    int batchDelete(List<E> list);

    /**
     * 根据主键更新覆盖
     *
     * @param entity 实体类
     * @return 影响条数
     */
    int updateByPrimaryKey(E entity);

    /**
     * 根据主键更新覆盖有值列
     *
     * @param entity 实体类
     * @return 影响条数
     */
    int updateByPrimaryKeySelective(E entity);

    /**
     * 根据主键更新覆盖指定列
     *
     * @param entity 实体类
     * @param fields 指定列
     * @return 影响条数
     */
    int updateField(E entity, String... fields);

    /**
     * 批量根据主键更新覆盖
     *
     * @param list 实体集合
     * @return 影响条数
     */
    int batchUpdate(List<E> list);

    /**
     * 批量根据主键更新有值列
     *
     * @param list 实体集合
     * @return 影响条数
     */
    int batchUpdateSelective(List<E> list);

    /**
     * 批量根据主键更新指定列
     *
     * @param list 实体集合
     * @param fields 指定列
     * @return 影响条数
     */
    int batchUpdateField(List<E> list, String... fields);

    /**
     * 根据主键查询
     *
     * @param entity 实体类
     * @return 查询结果
     */
    E selectByPrimaryKey(E entity);

    /**
     * 根据条件查询列表
     *
     * @param condition 条件
     * @return 查询结果
     */
    List<E> list(Condition<E> condition);

    /**
     * 根据条件查询列表
     *
     * @param entity 有值的为条件关系是等于
     * @return 查询结果
     */
    List<E> list(E entity);

    /**
     * 根据条件查询一个结果
     *
     * @param condition 条件
     * @return 查询结果
     */
    E detail(Condition<E> condition);

    /**
     * 根据条件查询一个结果
     *
     * @param entity 有值的为条件关系是等于
     * @return 查询结果
     */
    E detail(E entity);

    /**
     * 根据条件复杂查询出结果并转换格式
     *
     * @param resultTypeClass 返回结果类型class
     * @param leftJoin        查询条件
     * @return 查询结果
     */
    <R> List<R> list(Class<R> resultTypeClass, LeftJoin<E> leftJoin);

    /**
     * 根据条件复杂查询出结果并转换格式
     *
     * @param resultTypeClass 返回结果类型class
     * @param leftJoin        查询条件
     * @return 查询结果
     */
    <R> R detail(Class<R> resultTypeClass, LeftJoin<E> leftJoin);
}
