package bbw.ch.FairwayEcoBackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an invalid order state transition is attempted.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOrderStateException extends RuntimeException {

  public InvalidOrderStateException(String message) {
    super(message);
  }
}
