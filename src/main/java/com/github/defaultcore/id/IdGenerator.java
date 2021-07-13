package com.github.defaultcore.id;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * id生成器
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/7/13 8:59
 */
@UtilityClass
@Slf4j
public class IdGenerator {

    /**
     * id生成器map
     */
    public static final ConcurrentHashMap<String, SnowFlakeImpl> BUILDER = new ConcurrentHashMap<>();

    /**
     * 获取单个id
     *
     * @param dataCenterId 数据中心标识
     * @param machineId    机器标识
     * @return id
     */
    public Long get(Integer dataCenterId, Integer machineId) {
        SnowFlakeImpl snowFlake;
        String key = dataCenterId + "_" + machineId;
        synchronized (BUILDER) {
            if (BUILDER.containsKey(key)) {
                snowFlake = BUILDER.get(key);
            } else {
                snowFlake = new SnowFlakeImpl(dataCenterId, machineId);
                BUILDER.put(key, snowFlake);
            }
        }
        Long id = snowFlake.nextId();
        log.trace("数据中心：{}，机器：{}，生成id：{}", dataCenterId, machineId, id);
        return id;
    }

}
