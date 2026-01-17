package bbw.ch.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway Application for Fairway Eco.
 * Routes requests to backend microservices.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class FairwayEcoGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(FairwayEcoGatewayApplication.class, args);
  }

}
