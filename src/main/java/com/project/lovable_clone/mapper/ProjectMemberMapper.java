package com.project.lovable_clone.mapper;

import com.project.lovable_clone.dto.members.MemberResponse;
import com.project.lovable_clone.entity.ProjectMember;
import com.project.lovable_clone.entity.User;
import com.project.lovable_clone.enums.ProjectRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {
    @Mapping(target = "userId",source = "id")
    @Mapping(target = "projectRole",constant = "OWNER")
    MemberResponse toMemberResponseFromUser(User user);

    @Mapping(target = "userId",source = "user.id")
    @Mapping(target = "username",source = "user.username")
    @Mapping(target = "name",source = "user.name")
    MemberResponse toMemberResponseFromProjectMember(ProjectMember projectMember);
}
