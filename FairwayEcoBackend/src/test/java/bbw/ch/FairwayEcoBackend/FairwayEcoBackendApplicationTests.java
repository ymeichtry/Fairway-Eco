package bbw.ch.FairwayEcoBackend;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Basic application tests.
 * Full Spring context tests are skipped as they require Kafka broker.
 */
class FairwayEcoBackendApplicationTests {

	@Test
	void applicationClassExists() {
		// Verify that the main application class exists and can be loaded
		assertTrue(FairwayEcoBackendApplication.class.isAnnotationPresent(
				org.springframework.boot.autoconfigure.SpringBootApplication.class),
				"Application should have @SpringBootApplication annotation");
	}

}
