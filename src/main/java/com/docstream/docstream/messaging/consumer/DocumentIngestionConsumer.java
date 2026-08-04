package com.docstream.docstream.messaging.consumer;

import com.docstream.docstream.messaging.event.DocumentIngestionEvent;
import com.docstream.docstream.messaging.producer.DocumentIngestionProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DocumentIngestionConsumer {

    @KafkaListener(
            topics = DocumentIngestionProducer.TOPIC,
            groupId = "docstream-consumer-group"
    )
    public void consume(
            @Payload DocumentIngestionEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Received ingestion event - eventId: {} documentId: {} partition: {} offset: {}",
                event.eventId(), event.documentId(), partition, offset);

        try {
            processDocument(event);
            log.info("Successfully processed document: {}", event.documentId());
        } catch (Exception e) {
            log.error("Failed to process document: {} error: {}",
                    event.documentId(), e.getMessage());
            throw e;
        }
    }

    private void processDocument(DocumentIngestionEvent event) {
        log.info("Processing document: {} title: {}", event.documentId(), event.title());
        // Week 5: chunking and embedding will go here
    }
}
