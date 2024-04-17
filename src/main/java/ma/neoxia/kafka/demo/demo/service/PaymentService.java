package ma.neoxia.kafka.demo.demo.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.neoxia.kafka.demo.demo.domain.Payment;
import ma.neoxia.kafka.demo.demo.dto.CreateOrderDto;
import ma.neoxia.kafka.demo.demo.exception.DomainException;
import ma.neoxia.kafka.demo.demo.mapper.AppMapper;
import ma.neoxia.kafka.demo.demo.repository.PaymentRepository;
import ma.neoxia.kafka.demo.demo.repository.PaymentThresholdRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentThresholdRepository paymentThresholdRepository;
    private final KafkaTemplate<String, CreateOrderDto> kafkaTemplate;
    private final AppMapper appMapper;

    public void validatePayment(CreateOrderDto createOrderDto) {
        var threshold = paymentThresholdRepository.findByCustomerId(createOrderDto.customerId());
        if (threshold.isEmpty()) {
            throw new DomainException("user not authorized to complete payment");
        }
    }

    public Payment createPayment(CreateOrderDto createOrderDto) {
        var payment = appMapper.createOrderDtoToPayment(createOrderDto);
        return paymentRepository.save(payment);
    }

    @KafkaListener(
            topics = "neoxia-ma-demo-request-payment-v1.0",
            containerFactory = "createOrderKafkaListenerContainerFactory")
    public void preparePaymentOperation(CreateOrderDto createOrderCommand) throws InterruptedException {
        log.info("receiving message {}", createOrderCommand.toString());
        Thread.sleep(10000L);
        createPayment(createOrderCommand);
        log.info("payment created");
        var future = kafkaTemplate.send("neoxia-ma-demo-response-payment-v1.0", createOrderCommand);
        future.whenCompleteAsync((result, ex) -> {
            log.info("message sent to  neoxia-ma-demo-response-payment-v1.0");
        });
    }
}
