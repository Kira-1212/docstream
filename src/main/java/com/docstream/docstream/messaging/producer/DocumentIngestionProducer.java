package com.docstream.docstream.messaging.producer;

import com.docstream.docstream.messaging.event.DocumentIngestionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentIngestionProducer {

    public static final String TOPIC = "document-ingestion";
    private final KafkaTemplate<String, DocumentIngestionEvent> kafkaTemplate;

    public void publish(DocumentIngestionEvent event) {
        log.info("Publishing ingestion event for document: {} eventId: {}",
                event.documentId(), event.eventId());

        kafkaTemplate.send(TOPIC, event.documentId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event for document: {} error: {}",
                                event.documentId(), ex.getMessage());
                    } else {
                        log.info("Event published successfully for document: {} partition: {} offset: {}",
                                event.documentId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}