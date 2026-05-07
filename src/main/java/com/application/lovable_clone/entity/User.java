package com.application.lovable_clone.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.service.spi.InjectService;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

     @Column(nullable = false)
     String email;

     String passwordHash;

     String name;
     String avatarUrl;
     @CreationTimestamp
     Instant createdAt;

     @UpdateTimestamp
     Instant updatedAt;
     Instant deletedAt;



}
