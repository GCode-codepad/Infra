package org.example.documentservice.dto;

import lombok.Data;
import org.example.documentservice.model.AccessLevel;

@Data
public class CreateDocumentRequest {
    private Long roomId;
    private String title;
    private AccessLevel accessLevel;
}
