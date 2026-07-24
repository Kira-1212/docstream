package com.docstream.docstream.controller;

import com.docstream.docstream.model.Document;
import com.docstream.docstream.service.DocumentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<Document> createDocument(
            @Valid @RequestBody CreateDocumentRequest request) {
        log.info("POST /api/v1/documents - title: {}", request.title());
        Document document = documentService.createDocument(
                request.title(), request.content());
        return ResponseEntity.status(HttpStatus.CREATED).body(document);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable UUID id) {
        log.info("GET /api/v1/documents/{}", id);
        return ResponseEntity.ok(documentService.getDocument(id));
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments(
            @RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(documentService.getDocumentsByStatus(status));
        }
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(
            @PathVariable UUID id,
            @Valid @RequestBody CreateDocumentRequest request) {
        log.info("PUT /api/v1/documents/{}", id);
        return ResponseEntity.ok(
                documentService.updateDocument(id, request.title(), request.content()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        log.info("DELETE /api/v1/documents/{}", id);
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    public record CreateDocumentRequest(
            @NotBlank(message = "Title is required")
            @Size(max = 500, message = "Title must not exceed 500 characters")
            String title,

            String content
    ) {}
}