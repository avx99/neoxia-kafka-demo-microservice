package ma.neoxia.kafka.demo.demo.dto;

import lombok.Builder;

@Builder
public record OrderAddress(String street, String postalCode,
                           String city) {
}
