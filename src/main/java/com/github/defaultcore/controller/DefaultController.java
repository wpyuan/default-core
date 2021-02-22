package com.github.defaultcore.controller;

import com.github.defaultcore.service.DefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author PeiYuan
 */
public class DefaultController<E> {

    @Autowired
    private DefaultService<E> service;

    @PostMapping("/create")
    public ResponseEntity<E> create(@RequestBody E entity) {
        service.insertSelective(entity);
        return ResponseEntity.ok(entity);
    }

    @PostMapping("/create-form")
    public ResponseEntity<E> createByForm(E entity) {
        service.insertSelective(entity);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> remove(E entity) {
        service.delete(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<E> update(E entity) {
        service.updateByPrimaryKey(entity);
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/detail")
    public ResponseEntity<E> detail(E entity) {
        return ResponseEntity.ok(service.detail(entity));
    }

    @GetMapping("/list")
    public ResponseEntity<List<E>> list(E entity) {
        return ResponseEntity.ok(service.list(entity));
    }
}
