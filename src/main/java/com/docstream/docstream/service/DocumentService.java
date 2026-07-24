package com.docstream.docstream.service;

import com.docstream.docstream.model.Document;
import com.docstream.docstream.repository.DocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Transactional
    public Document createDocument(String title, String content) {
        log.info("Creating document with title: {}", title);
        Document document = Document.builder()
                .title(title)
                .content(content)
                .status("PENDING")
                .build();
        Document saved = documentRepository.save(document);
        log.info("Document created with id: {}", saved.getId());
        return saved;
    }

    public Document getDocument(UUID id) {
        log.info("Fetching document with id: {}", id);
        return documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Document not found with id: " + id));
    }

    public List<Document> getAllDocuments() {
        log.info("Fetching all documents");
        return documentRepository.findAll();
    }

    public List<Document> getDocumentsByStatus(String status) {
        log.info("Fetching documents with status: {}", status);
        return documentRepository.findByStatus(status);
    }

    @Transactional
    public Document updateDocument(UUID id, String title, String content) {
        log.info("Updating document with id: {}", id);
        Document document = getDocument(id);
        document.setTitle(title);
        document.setContent(content);
        return documentRepository.save(document);
    }

    @Transactional
    public void deleteDocument(UUID id) {
        log.info("Deleting document with id: {}", id);
        if (!documentRepository.existsById(id)) {
            throw new EntityNotFoundException("Document not found with id: " + id);
        }
        documentRepository.deleteById(id);
    }
}