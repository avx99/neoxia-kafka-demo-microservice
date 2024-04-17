package ma.neoxia.kafka.demo.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ma.neoxia.kafka.demo.demo.enums.OrderStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderCreationResponse {
    private String orderId;
    private String message;
    private OrderStatus orderStatus;
}
