package com.example.frontend.controller;

import brave.propagation.ExtraFieldPropagation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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


    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String call(){
        ExtraFieldPropagation.set("groupId","111");
        log.info("in call set groupId->{}",ExtraFieldPropagation.get("groupId"));
        String res =  restTemplate.getForObject("http://backend/call",String.class);
        log.info("groupId->{}", ExtraFieldPropagation.get("groupId"));
        return res;
    }
}
