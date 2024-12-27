package org.example.documentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rooms")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique identifier that clients use to join a collaboration session
    @Column(name = "room_uuid", nullable = false, unique = true)
    private String roomUuid;

    // If your domain logic says each Room corresponds to a single Document,
    // then you can keep this reference. If it's many Documents per Room, adjust accordingly.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;
}
