package com.project.lovable_clone.entity;

import com.project.lovable_clone.enums.ProjectRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.CloseableThreadContext;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectMember {
    ProjectMemberId id;
    User user;
    Project project;
    ProjectRole role;
    Instant invitedAt;
    Instant acceptedAt;
}
