package com.application.lovable_clone.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.service.spi.InjectService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

     @Column(nullable = false)
     String username;

     String password;

     String name;

     @Column(unique = true)
     String stripeCustomerId;

     @CreationTimestamp
     Instant createdAt;

     @UpdateTimestamp
     Instant updatedAt;
     Instant deletedAt;


     @Override
     public Collection<? extends GrantedAuthority> getAuthorities() {
          return List.of();
     }
}
