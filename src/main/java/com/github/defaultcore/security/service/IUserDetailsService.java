package com.github.defaultcore.security.service;

import com.github.defaultcore.security.pojo.DefaultUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

/**
 * <p>
 *     查询填充userDetail服务
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/4/22 17:30
 */
public interface IUserDetailsService {
    /**
     * 获取用户
     * @param userName 用户名
     * @return 数据库存在的用户信息
     */
    DefaultUser getDefaultUser(String userName);

    /**
     * 添加权限
     * @param authorities 权限集合
     */
    void setAuthorities(Collection<SimpleGrantedAuthority> authorities);

}
