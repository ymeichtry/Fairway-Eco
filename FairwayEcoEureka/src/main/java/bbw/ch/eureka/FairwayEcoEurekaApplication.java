package bbw.ch.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka Service Discovery Server.
 * Manages service registration and discovery for microservices.
 */
@SpringBootApplication
@EnableEurekaServer
public class FairwayEcoEurekaApplication {

  public static void main(String[] args) {
    SpringApplication.run(FairwayEcoEurekaApplication.class, args);
  }

}
