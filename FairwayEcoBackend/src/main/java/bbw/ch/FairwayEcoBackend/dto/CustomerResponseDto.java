package bbw.ch.FairwayEcoBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning customer information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {

  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private AddressDto address;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
