package ms_campuslab_audit.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejo de errores del consumidor de Kafka.
 *
 * Un evento que falla se reintenta 2 veces (1 s entre intentos). Si sigue fallando, o si el
 * payload es inválido (IllegalArgumentException, que no se reintenta), se publica en el tópico
 * "<tópico>.DLT" y el consumidor continúa con el siguiente mensaje: un mensaje defectuoso no
 * bloquea la partición.
 */
@Slf4j
@Configuration
public class KafkaErrorConfig {

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(
            KafkaTemplate<Object, Object> jsonTemplate,
            ProducerFactory<Object, Object> producerFactory
    ) {

        // Los mensajes que no se pudieron deserializar llegan como bytes: se reenvían sin alterarlos.
        KafkaTemplate<Object, Object> bytesTemplate = new KafkaTemplate<>(
            producerFactory.copyWithConfigurationOverride(
                Map.of(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class)
            )
        );

        Map<Class<?>, KafkaOperations<?, ?>> templates = new LinkedHashMap<>();
        templates.put(byte[].class, bytesTemplate);
        templates.put(Object.class, jsonTemplate);

        DeadLetterPublishingRecoverer deadLetterRecoverer = new DeadLetterPublishingRecoverer(templates);

        ConsumerRecordRecoverer recoverer = (record, exception) -> {
            log.error(
                "Kafka processing failed. Sending to DLT. topic={}, partition={}, offset={}",
                record.topic(),
                record.partition(),
                record.offset(),
                exception
            );
            deadLetterRecoverer.accept(record, exception);
        };

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
            recoverer,
            new FixedBackOff(1000L, 2L)
        );

        // Un payload inválido no se arregla reintentando: va directo a la DLT.
        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);

        return errorHandler;
    }
}
