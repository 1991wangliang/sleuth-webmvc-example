package com.example.frontend.rule;

import brave.propagation.ExtraFieldPropagation;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/17
 *
 * @author codingapi
 */
@Component
@Slf4j
@Scope("request")
public class MyRule extends ZoneAvoidanceRule {

    private final static String KEY = "groupId";

    @Override
    public Server choose(Object key) {
        ExtraFieldPropagation.set(KEY,"123");
        log.info("in choose set groupId->{}",ExtraFieldPropagation.get(KEY));
        Server server =  super.choose(key);
        ExtraFieldPropagation.set(KEY,"456");
        log.info("in choose set groupId->{}",ExtraFieldPropagation.get(KEY));
        return server;
    }
}
