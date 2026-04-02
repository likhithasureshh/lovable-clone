package com.project.lovable_clone.entity;

import com.project.lovable_clone.enums.ProjectRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.CloseableThreadContext;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_members")
@Builder
public class ProjectMember {
    @EmbeddedId
    ProjectMemberId id;
    @ManyToOne
    @MapsId("userId")
    User user;
    @ManyToOne
    @MapsId("projectId")
    Project project;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ProjectRole role;
    Instant invitedAt;
    Instant acceptedAt;
}
