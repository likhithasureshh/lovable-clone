package com.project.lovable_clone.entity;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectMemberId {
    Long userId;
    Long projectId;
}
