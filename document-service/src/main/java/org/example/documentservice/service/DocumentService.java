package org.example.documentservice.service;

import jakarta.transaction.Transactional;
import org.example.documentservice.dto.DocumentResponse;
import org.example.documentservice.dto.RoomInfo;
import org.example.documentservice.model.AccessLevel;
import org.example.documentservice.model.Document;
import org.example.documentservice.model.Room;
import org.example.documentservice.repository.DocumentRepository;
import org.example.documentservice.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    private RoomRepository roomRepository;

    @Autowired
    private UserClient userClient;

    /**
     * Create a new Document with a given title, roomId, and AccessLevel.
     */
    @Transactional
    public Document createDocument(String title, Long roomId, AccessLevel accessLevel) {
        Document doc = new Document();
        doc.setTitle(title);
        doc.setRoomId(roomId);
        doc.setAccessLevel(accessLevel);
        doc.setCreatedAt(Instant.now());
        doc.setUpdatedAt(Instant.now());
        return documentRepository.save(doc);
    }

    /**
     * Retrieve document by ID (no user checks here).
     */
    @Transactional
    public Optional<Document> getDocument(Long documentId) {
        return documentRepository.findById(documentId);
    }

    /**
     * Update the document's title or access level.
     */
    @Transactional
    public Optional<Document> updateDocument(Long documentId, String newTitle, AccessLevel newLevel) {
        return documentRepository.findById(documentId).map(doc -> {
            doc.setTitle(newTitle);
            doc.setAccessLevel(newLevel);
            doc.setUpdatedAt(Instant.now());
            return documentRepository.save(doc);
        });
    }

    /**
     * Delete the document by ID.
     */
    @Transactional
    public void deleteDocument(Long documentId) {
        documentRepository.deleteById(documentId);
    }

    /**
     * Create a Room. If each Room must correspond to a Document,
     * we can link them here.
     */
    @Transactional
    public Room createRoom(Long documentId) {
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // Generate a random UUID for the room
        String roomUuid = UUID.randomUUID().toString();

        Room room = new Room();
        room.setRoomUuid(roomUuid);
        room.setDocument(doc);

        return roomRepository.save(room);
    }

    /**
     * Find a room by its UUID.
     */
    @Transactional
    public Optional<Room> getRoomByUuid(String roomUuid) {
        return Optional.ofNullable(roomRepository.findByRoomUuid(roomUuid));
    }

    /**
     * Delete the room by ID.
     */
    @Transactional
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
