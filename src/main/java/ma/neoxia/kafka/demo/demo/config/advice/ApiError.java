package ma.neoxia.kafka.demo.demo.config.advice;

import lombok.Builder;

@Builder
public record ApiError(String message) {
}
