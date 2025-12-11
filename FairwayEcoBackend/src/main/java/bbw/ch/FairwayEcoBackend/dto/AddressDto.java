package bbw.ch.FairwayEcoBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for address information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

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
