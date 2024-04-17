package ma.neoxia.kafka.demo.demo.repository;

import ma.neoxia.kafka.demo.demo.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByCustomerId(UUID customerId);
}
