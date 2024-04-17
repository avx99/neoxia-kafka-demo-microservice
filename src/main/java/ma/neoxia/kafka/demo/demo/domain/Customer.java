package ma.neoxia.kafka.demo.demo.domain;

import lombok.*;

import jakarta.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers", schema = "nxm")
@Entity
public class Customer {
    @Id
    private UUID id;
}

