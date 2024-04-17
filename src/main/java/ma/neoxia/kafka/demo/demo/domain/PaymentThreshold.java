package ma.neoxia.kafka.demo.demo.domain;


import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments_threshold", schema = "nxm")
@Entity
public class PaymentThreshold {
    @Id
    private UUID id;
    private UUID customerId;
    private UUID orderId;
    private BigDecimal totalAmount;
}
