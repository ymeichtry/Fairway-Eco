package bbw.ch.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Fallback controller for circuit breaker.
 * Returns fallback responses when backend services are unavailable.
 */
@RestController
@RequestMapping("/fallback")
@Slf4j
public class FallbackController {

  @GetMapping("/golfballs")
  public ResponseEntity<Map<String, Object>> golfballsFallback() {
    log.warn("Golf Ball service is unavailable, returning fallback response");
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Golf Ball service is temporarily unavailable. Please try again later.");
    response.put("timestamp", LocalDateTime.now());
    response.put("data", new Object[] {});
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
  }

  @GetMapping("/customers")
  public ResponseEntity<Map<String, Object>> customersFallback() {
    log.warn("Customer service is unavailable, returning fallback response");
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Customer service is temporarily unavailable. Please try again later.");
    response.put("timestamp", LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
  }

  @GetMapping("/orders")
  public ResponseEntity<Map<String, Object>> ordersFallback() {
    log.warn("Order service is unavailable, returning fallback response");
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Order service is temporarily unavailable. Please try again later.");
    response.put("timestamp", LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
  }
}
