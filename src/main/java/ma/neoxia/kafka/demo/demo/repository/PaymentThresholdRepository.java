package ma.neoxia.kafka.demo.demo.repository;

import ma.neoxia.kafka.demo.demo.domain.PaymentThreshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentThresholdRepository extends JpaRepository<PaymentThreshold, UUID> {
    Optional<PaymentThreshold> findByCustomerId(UUID customerId);
}
