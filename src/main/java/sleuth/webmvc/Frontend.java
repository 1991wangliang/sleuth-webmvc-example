package sleuth.webmvc;

import brave.Tracing;
import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import com.alibaba.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
@CrossOrigin // So that javascript can be hosted elsewhere
public class Frontend {

  private Logger logger = LoggerFactory.getLogger(Frontend.class);

  @Reference(url = "dubbo://127.0.0.1:9000")
  Api api;


  @RequestMapping("/") public String callBackend() {
    String str =  api.printDate();
    ExtraFieldPropagation.set("groupId","test");
    logger.info("str->{}",str);
    return str;
  }


  @Bean
  public Tracing tracing(){
    return Tracing.newBuilder()
            .propagationFactory(ExtraFieldPropagation.newFactory(B3Propagation.FACTORY,"groupId"))
            .build();
  }

  public static void main(String[] args) {
    SpringApplication.run(Frontend.class,
            "--spring.application.name=frontend",
            // redundant https://github.com/apache/incubator-dubbo-spring-boot-project/issues/321
            "--dubbo.application.name=backend",
            "--server.port=8081",
            "--spring.sleuth.propagation-keys=groupId"
    );
  }
}
