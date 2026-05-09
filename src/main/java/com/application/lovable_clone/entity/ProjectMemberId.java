package com.application.lovable_clone.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberId {
    Long projectId;
    Long userId;
}
