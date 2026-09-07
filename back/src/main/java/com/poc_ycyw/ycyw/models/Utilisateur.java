package com.poc_ycyw.ycyw.models;

import jakarta.persistence.*;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.sql.Timestamp;

@Data
@Entity
@Table(name = "utilisateurs")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, unique = true)
    String email;

    String name;

    @Column(nullable = false)
    String password;

    @Column(nullable = false, length = 10)
    String role;

    @CreationTimestamp
    @Column(updatable = false)
    Timestamp createdAt;

    @UpdateTimestamp
    Timestamp updatedAt;

}
