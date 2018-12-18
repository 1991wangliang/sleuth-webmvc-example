package com.example.frontend.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/18
 *
 * @author codingapi
 */
@Slf4j
@Component
public class RestTemplateListener implements ApplicationListener<ContextRefreshedEvent>, Ordered {

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if(interceptors.size()>0) {
            log.info("interceptors->{}", interceptors);
            ClientHttpRequestInterceptor balancerInterceptor = null;
            for (int index = 0; index < interceptors.size(); index++) {
                ClientHttpRequestInterceptor interceptor = interceptors.get(index);
                if (index != 0 && interceptor instanceof LoadBalancerInterceptor) {
                    balancerInterceptor = interceptor;
                }
            }

            if (balancerInterceptor != null) {
                interceptors.remove(balancerInterceptor);
                interceptors.add(0, balancerInterceptor);
                log.info("interceptors->{}", interceptors);
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
