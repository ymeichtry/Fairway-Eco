package bbw.ch.FairwayEcoBackend.mapper;

import bbw.ch.FairwayEcoBackend.dto.AddressDto;
import bbw.ch.FairwayEcoBackend.dto.CustomerCreateDto;
import bbw.ch.FairwayEcoBackend.dto.CustomerResponseDto;
import bbw.ch.FairwayEcoBackend.model.Address;
import bbw.ch.FairwayEcoBackend.model.Customer;
import org.springframework.stereotype.Component;

/**
 * Mapper for Customer entity and DTOs.
 */
@Component
public class CustomerMapper {

  public Customer toEntity(CustomerCreateDto dto) {
    return Customer.builder()
        .firstName(dto.getFirstName())
        .lastName(dto.getLastName())
        .email(dto.getEmail())
        .phone(dto.getPhone())
        .address(toAddressEntity(dto.getAddress()))
        .build();
  }

  public CustomerResponseDto toResponseDto(Customer entity) {
    return CustomerResponseDto.builder()
        .id(entity.getId())
        .firstName(entity.getFirstName())
        .lastName(entity.getLastName())
        .email(entity.getEmail())
        .phone(entity.getPhone())
        .address(toAddressDto(entity.getAddress()))
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  public void updateEntityFromDto(CustomerCreateDto dto, Customer entity) {
    entity.setFirstName(dto.getFirstName());
    entity.setLastName(dto.getLastName());
    entity.setEmail(dto.getEmail());
    entity.setPhone(dto.getPhone());
    entity.setAddress(toAddressEntity(dto.getAddress()));
  }

  public Address toAddressEntity(AddressDto dto) {
    if (dto == null)
      return null;
    return Address.builder()
        .street(dto.getStreet())
        .city(dto.getCity())
        .postalCode(dto.getPostalCode())
        .country(dto.getCountry())
        .build();
  }

  public AddressDto toAddressDto(Address entity) {
    if (entity == null)
      return null;
    return AddressDto.builder()
        .street(entity.getStreet())
        .city(entity.getCity())
        .postalCode(entity.getPostalCode())
        .country(entity.getCountry())
        .build();
  }
}
