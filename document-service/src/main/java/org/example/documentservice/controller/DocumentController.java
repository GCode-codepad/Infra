package org.example.documentservice.controller;

import org.example.documentservice.dto.CreateDocumentRequest;
import org.example.documentservice.dto.DocumentResponse;
import org.example.documentservice.model.Document;
import org.example.documentservice.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(@RequestBody CreateDocumentRequest request) {
        Document doc = documentService.createDocument(request.getRoomId(), request.getTitle(), request.getAccessLevel());
        DocumentResponse response = documentService.buildDocumentResponse(doc);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocument(@PathVariable Long id) {
        return documentService.getDocumentById(id)
                .map(doc -> ResponseEntity.ok(documentService.buildDocumentResponse(doc)))
                .orElse(ResponseEntity.notFound().build());
    }
}
