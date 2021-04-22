package com.github.defaultcore.security.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *     默认user结构
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/4/22 17:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DefaultUser {
    private String userName;
    private String password;
}
