package com.github.defaultcore.service.impl;

import com.github.defaultcore.service.DefaultService;
import com.github.mybatis.crud.mapper.BatchInsertMapper;
import com.github.mybatis.crud.mapper.DefaultMapper;
import com.github.mybatis.crud.structure.Condition;
import com.github.mybatis.crud.structure.LeftJoin;
import com.github.mybatis.crud.util.EntityUtil;
import com.github.mybatis.crud.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author PeiYuan
 */
public class DefaultServiceImpl<E> implements DefaultService<E> {

    @Autowired
    private DefaultMapper<E> defaultMapper;
    @Autowired
    private BatchInsertMapper<E> batchInsertMapper;

    @Override
    public Boolean insert(E entity) {
        return defaultMapper.insert(entity) == 1;
    }

    @Override
    public Boolean insertSelective(E entity) {
        return defaultMapper.insertSelective(entity) == 1;
    }

    @Override
    public void batchInsert(List<E> list) {
        batchInsertMapper.batchInsert(list);
    }

    @Override
    public void batchInsertSelective(List<E> list) {
        batchInsertMapper.batchInsertSelective(list);
    }

    @Override
    public int deleteByPrimaryKey(E entity) {
        return defaultMapper.deleteByPrimaryKey(entity);
    }

    @Override
    public int delete(E entity) {
        return defaultMapper.delete(this.createEqCondition(entity));
    }

    @Override
    public int batchDelete(List<E> list) {
        return defaultMapper.batchDelete(list);
    }

    @Override
    public int updateByPrimaryKey(E entity) {
        return defaultMapper.updateByPrimaryKey(entity);
    }

    @Override
    public int updateByPrimaryKeySelective(E entity) {
        return defaultMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int updateField(E entity, String... fields) {
        return defaultMapper.updateField(entity, fields);
    }

    @Override
    public int batchUpdate(List<E> list) {
        int updateCount = 0;
        for (E entity : list) {
            updateCount += defaultMapper.updateByPrimaryKey(entity);
        }
        return updateCount;
    }

    @Override
    public int batchUpdateSelective(List<E> list) {
        int updateCount = 0;
        for (E entity : list) {
            updateCount += defaultMapper.updateByPrimaryKeySelective(entity);
        }
        return updateCount;
    }

    @Override
    public int batchUpdateField(List<E> list, String... fields) {
        int updateCount = 0;
        for (E entity : list) {
            updateCount += defaultMapper.updateField(entity, fields);
        }
        return updateCount;
    }

    @Override
    public E selectByPrimaryKey(E entity) {
        return defaultMapper.selectByPrimaryKey(entity);
    }

    @Override
    public List<E> list(Condition<E> condition) {
        return defaultMapper.list(condition);
    }

    @Override
    public List<E> list(E entity) {
        return this.list(this.createEqCondition(entity));
    }

    @Override
    public E detail(Condition<E> condition) {
        return defaultMapper.detail(condition);
    }

    @Override
    public E detail(E entity) {
        return this.detail(this.createEqCondition(entity));
    }

    @Override
    public <R> List<R> list(Class<R> resultTypeClass, LeftJoin<E> leftJoin) {
        return defaultMapper.list(resultTypeClass, leftJoin);
    }

    @Override
    public <R> R detail(Class<R> resultTypeClass, LeftJoin<E> leftJoin) {
        return defaultMapper.detail(resultTypeClass, leftJoin);
    }

    /**
     * 由entity有值字段作为等于关系条件构造Condition
     * @param entity 实体类有值字段作为等于关系条件
     * @return Condition条件
     */
    public Condition<E> createEqCondition(E entity) {
        Condition<E> condition = Condition.<E>builder(entity).build();
        List<String> eqFields = EntityUtil.getAllHaveValueFieldName(entity, null);
        Method eq = ReflectionUtil.findMethod(condition.getClass(), "eq", String.class);
        ReflectionUtil.makeAccessible(eq);
        for (String eqField: eqFields) {
            condition = (Condition<E>) ReflectionUtil.invokeMethod(eq, condition, eqField);
        }

        return condition;
    }
}
