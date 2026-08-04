package com.docstream.docstream.messaging.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentIngestionEvent(
        UUID eventId,
        UUID documentId,
        String title,
        String content,
        LocalDateTime occurredAt
) {
    public static DocumentIngestionEvent of(UUID documentId, String title, String content) {
        return new DocumentIngestionEvent(
                UUID.randomUUID(),
                documentId,
                title,
                content,
                LocalDateTime.now()
        );
    }
}