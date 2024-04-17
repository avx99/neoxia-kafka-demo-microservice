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
@Table(name = "payments", schema = "nxm")
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID customerId;
    private UUID orderId;
    private BigDecimal amount;
}
