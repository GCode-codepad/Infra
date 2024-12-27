package org.example.documentservice.controller;

import org.example.documentservice.dto.CreateDocumentRequest;
import org.example.documentservice.dto.DocumentResponse;
import org.example.documentservice.model.AccessLevel;
import org.example.documentservice.model.Document;
import org.example.documentservice.model.Room;
import org.example.documentservice.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    /**
     * Create a new Document.
     */
    @PostMapping
    public ResponseEntity<Document> createDocument(
            @RequestParam String title,
            @RequestParam Long roomId,
            @RequestParam AccessLevel accessLevel
    ) {
        Document doc = documentService.createDocument(title, roomId, accessLevel);
        return ResponseEntity.ok(doc);
    }

    /**
     * Get a Document by ID.
     */
    @GetMapping("/{documentId}")
    public ResponseEntity<Document> getDocument(@PathVariable Long documentId) {
        Optional<Document> doc = documentService.getDocument(documentId);
        return doc.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Update a Document.
     */
    @PutMapping("/{documentId}")
    public ResponseEntity<Document> updateDocument(
            @PathVariable Long documentId,
            @RequestParam String newTitle,
            @RequestParam AccessLevel newAccessLevel
    ) {
        Optional<Document> updated = documentService.updateDocument(documentId, newTitle, newAccessLevel);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete a Document by ID.
     */
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Create a new Room linked to a given Document.
     */
    @PostMapping("/{documentId}/rooms")
    public ResponseEntity<Room> createRoom(@PathVariable Long documentId) {
        Room room = documentService.createRoom(documentId);
        return ResponseEntity.ok(room);
    }

    /**
     * Find a Room by UUID.
     */
    @GetMapping("/rooms/{roomUuid}")
    public ResponseEntity<Room> getRoom(@PathVariable String roomUuid) {
        return documentService.getRoomByUuid(roomUuid)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete a Room by ID.
     */
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        documentService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}
