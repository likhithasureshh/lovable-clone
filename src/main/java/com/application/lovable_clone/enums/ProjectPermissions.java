package com.application.lovable_clone.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProjectPermissions {
    VIEW("project-view"),
    EDIT("project-edit"),
    DELETE("project-delete"),
    MANAGE_MEMBERS("projectmember-manage_members"),
    VIEW_MEMBERS("projectmember-view_members"),
    EDIT_MEMBERS("projectmember-edit-members");


    private final String values;
}
