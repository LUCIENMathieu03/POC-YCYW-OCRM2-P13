package com.poc_ycyw.ycyw.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, columnDefinition = "TEXT")
    String content;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    Timestamp dateEnvoi;

    @Column(nullable = false)
    Integer utilisateurId;

    Integer destinataire;
}
