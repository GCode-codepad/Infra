package org.example.documentservice.dto;

import lombok.Builder;
import lombok.Data;
import org.example.documentservice.model.AccessLevel;

import java.time.Instant;

@Data
@Builder
public class DocumentResponse {
    private Long id;
    private String title;
    private AccessLevel accessLevel;
    private RoomInfo roomInfo;
    private Instant createdAt;
    private Instant updatedAt;
}
