package com.github.defaultcore.service.impl;

import com.github.defaultcore.service.DefaultService;
import com.github.mybatis.crud.mapper.DefaultMapper;
import com.github.mybatis.crud.structure.Condition;
import com.github.mybatis.crud.structure.LeftJoin;
import com.github.mybatis.crud.util.EntityUtil;
import com.github.mybatis.crud.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author PeiYuan
 */
public class DefaultServiceImpl<E> implements DefaultService<E> {

    @Autowired
    private DefaultMapper<E> defaultMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean insert(E entity) {
        return defaultMapper.insert(entity) == 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean insertSelective(E entity) {
        return defaultMapper.insertSelective(entity) == 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteByPrimaryKey(E entity) {
        return defaultMapper.deleteByPrimaryKey(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int delete(E entity) {
        return defaultMapper.delete(this.createEqCondition(entity));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateByPrimaryKey(E entity) {
        return defaultMapper.updateByPrimaryKey(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(E entity) {
        return defaultMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateField(E entity, String... fields) {
        return defaultMapper.updateField(entity, fields);
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
    private Condition<E> createEqCondition(E entity) {
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
