package org.example.editingservice.dto;

import lombok.Data;

@Data
public class DocEditDelta {
    private Long docId;
    private Long userId;
    private String operationType; // e.g. "insert", "delete", "format"
    private String content;       // the actual inserted/removed text or format info
    private long timestamp;       // client-side timestamp or server timestamp
}
