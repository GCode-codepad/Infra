package org.example.documentservice.service;

import org.example.documentservice.dto.DocumentResponse;
import org.example.documentservice.dto.RoomInfo;
import org.example.documentservice.model.AccessLevel;
import org.example.documentservice.model.Document;
import org.example.documentservice.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserClient userClient;

    public Document createDocument(Long roomId, String title, AccessLevel accessLevel) {
        Document doc = new Document();
        doc.getRoomId(roomId);
        doc.setTitle(title);
        doc.setAccessLevel(accessLevel);
        doc.setCreatedAt(Instant.now());
        doc.setUpdatedAt(Instant.now());
        return documentRepository.save(doc);
    }

    public Optional<Document> getDocumentById(Long docId) {
        return documentRepository.findById(docId);
    }

    public DocumentResponse buildDocumentResponse(Document doc) {
        // 调用用户服务获取所有者信息
        RoomInfo roomInfo = userClient.getUserInfo(doc.getRoomId());

        return DocumentResponse.builder()
                .id(doc.getId())
                .title(doc.getTitle())
                .accessLevel(doc.getAccessLevel())
                .roomInfo(roomInfo)
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }
}
