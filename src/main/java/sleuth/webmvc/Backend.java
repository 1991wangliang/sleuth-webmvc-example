package sleuth.webmvc;

import brave.Tracing;
import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;

@EnableAutoConfiguration
@EnableDubbo // redundant https://github.com/apache/incubator-dubbo-spring-boot-project/issues/322
@Service(interfaceClass = Api.class)
public class Backend implements Api {

  private Logger logger = LoggerFactory.getLogger(Backend.class);

  @Override
  public String printDate() {
    String str =  new Date().toString();
    String groupId =  ExtraFieldPropagation.get("groupId");
    logger.info("printDate->{},groupId->{}",str,groupId);
    return str;
  }

  @Bean
  public Tracing tracing(){
    return Tracing.newBuilder()
            .propagationFactory(ExtraFieldPropagation.newFactory(B3Propagation.FACTORY,"groupId"))
            .build();
  }

  public static void main(String[] args) {
    SpringApplication.run(Backend.class,
            "--spring.application.name=backend",
            // redundant https://github.com/apache/incubator-dubbo-spring-boot-project/issues/321
            "--dubbo.application.name=backend",
            // These args allow dubbo to start without any web framework
            "--spring.main.web-environment=false",
            "--dubbo.registry.address=N/A",
            "--dubbo.protocol.port=9000",
            "--spring.sleuth.propagation-keys=groupId"
    );
  }
}
