package com.application.lovable_clone.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.application.lovable_clone.enums.ProjectPermissions.*;

@RequiredArgsConstructor
@Getter
public enum ProjectRole {
    EDITOR(Set.of(VIEW,EDIT,VIEW_MEMBERS,EDIT_MEMBERS)),
    VIEWER(Set.of(VIEW,VIEW_MEMBERS)),
    OWNER(Set.of(VIEW,EDIT,DELETE,VIEW_MEMBERS,EDIT_MEMBERS,MANAGE_MEMBERS));

    private final Set<ProjectPermissions> permissions;
}
