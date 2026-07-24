package com.docstream.docstream.service;

import com.docstream.docstream.model.Document;
import com.docstream.docstream.repository.DocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    private Document testDocument;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testDocument = Document.builder()
                .id(testId)
                .title("Test Document")
                .content("Test content")
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createDocument_shouldReturnSavedDocument() {
        when(documentRepository.save(any(Document.class))).thenReturn(testDocument);

        Document result = documentService.createDocument("Test Document", "Test content");

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Document");
        assertThat(result.getStatus()).isEqualTo("PENDING");
        verify(documentRepository, times(1)).save(any(Document.class));
    }

    @Test
    void getDocument_whenExists_shouldReturnDocument() {
        when(documentRepository.findById(testId)).thenReturn(Optional.of(testDocument));

        Document result = documentService.getDocument(testId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getTitle()).isEqualTo("Test Document");
    }

    @Test
    void getDocument_whenNotExists_shouldThrowEntityNotFoundException() {
        when(documentRepository.findById(testId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentService.getDocument(testId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(testId.toString());
    }

    @Test
    void getAllDocuments_shouldReturnAllDocuments() {
        when(documentRepository.findAll()).thenReturn(List.of(testDocument));

        List<Document> result = documentService.getAllDocuments();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Document");
    }

    @Test
    void deleteDocument_whenNotExists_shouldThrowEntityNotFoundException() {
        when(documentRepository.existsById(testId)).thenReturn(false);

        assertThatThrownBy(() -> documentService.deleteDocument(testId))
                .isInstanceOf(EntityNotFoundException.class);

        verify(documentRepository, never()).deleteById(any());
    }

    @Test
    void deleteDocument_whenExists_shouldDeleteSuccessfully() {
        when(documentRepository.existsById(testId)).thenReturn(true);
        doNothing().when(documentRepository).deleteById(testId);

        assertThatNoException().isThrownBy(() -> documentService.deleteDocument(testId));

        verify(documentRepository, times(1)).deleteById(testId);
    }
}