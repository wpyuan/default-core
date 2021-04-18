package com.github.defaultcore.controller;

import com.github.defaultcore.domain.entity.Config;
import com.github.defaultcore.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  系统配置 控制器
 *
 * @author wangpeiyuan
 * @date 2021-04-13 15:21:54
 */
@RestController
@RequestMapping("/config")
@Slf4j
public class ConfigController extends DefaultController<Config>{

    @Autowired
    private ConfigService service;

}