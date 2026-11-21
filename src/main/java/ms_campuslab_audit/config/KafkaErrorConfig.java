package ms_campuslab_audit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaErrorConfig {

    @Bean
    public DefaultErrorHandler kafkaErrorHandler() {

        return new DefaultErrorHandler(
            (record, exception) -> {
                log.error(
                    "Kafka processing failed. topic={}, partition={}, offset={}",
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    exception
                );

                throw new IllegalStateException(
                    "Kafka record processing failed",
                    exception
                );
            },
            new FixedBackOff(1000L, 2L)
        );
    }
}

