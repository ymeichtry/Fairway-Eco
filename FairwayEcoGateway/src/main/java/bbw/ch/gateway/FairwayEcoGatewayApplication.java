package bbw.ch.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway Application for Fairway Eco.
 * Routes requests to backend microservices.
 */
@SpringBootApplication
public class FairwayEcoGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(FairwayEcoGatewayApplication.class, args);
  }

}
