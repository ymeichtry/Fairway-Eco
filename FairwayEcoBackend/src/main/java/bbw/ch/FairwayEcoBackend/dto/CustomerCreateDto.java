package bbw.ch.FairwayEcoBackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new customer.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreateDto {

  @NotBlank(message = "First name is required")
  @Size(max = 50)
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Size(max = 50)
  private String lastName;

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @Size(max = 20)
  private String phone;

  @Valid
  private AddressDto address;
}
