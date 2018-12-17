package com.example.backend.controller;

import brave.propagation.ExtraFieldPropagation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/17
 *
 * @author codingapi
 */
@RestController
@Slf4j
public class DemoController {

    @GetMapping("/call")
    public String call(){
        log.info("groupId->{}", ExtraFieldPropagation.get("groupId"));
        return String.valueOf(System.currentTimeMillis());
    }
}
