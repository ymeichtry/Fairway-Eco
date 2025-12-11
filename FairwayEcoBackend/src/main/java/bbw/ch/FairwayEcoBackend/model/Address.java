package bbw.ch.FairwayEcoBackend.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Embeddable address component for customers.
 */
@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

  @NotBlank(message = "Street is required")
  @Size(max = 255)
  private String street;

  @NotBlank(message = "City is required")
  @Size(max = 100)
  private String city;

  @NotBlank(message = "Postal code is required")
  @Size(max = 10)
  private String postalCode;

  @NotBlank(message = "Country is required")
  @Size(max = 100)
  private String country;
}
