package bbw.ch.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Basic application tests for Gateway.
 */
@SpringBootTest
class FairwayEcoGatewayApplicationTests {

  @Test
  void contextLoads() {
    assertTrue(true, "Gateway application context should load successfully");
  }

  @Test
  void applicationClassExists() {
    assertTrue(FairwayEcoGatewayApplication.class.isAnnotationPresent(
        org.springframework.boot.autoconfigure.SpringBootApplication.class),
        "Application should have @SpringBootApplication annotation");
  }

}
