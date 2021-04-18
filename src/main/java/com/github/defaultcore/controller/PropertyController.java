package com.github.defaultcore.controller;

import com.github.defaultcore.domain.entity.Property;
import com.github.defaultcore.service.PropertyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  属性值 控制器
 *
 * @author wangpeiyuan
 * @date 2021-04-13 15:33:21
 */
@RestController
@RequestMapping("/property")
@Slf4j
public class PropertyController extends DefaultController<Property>{

    @Autowired
    private PropertyService service;

    @GetMapping("/list/{code}")
    public ResponseEntity<List<Property>> list(@PathVariable String code) {
        return ResponseEntity.ok(service.getProperties(code));
    }

}