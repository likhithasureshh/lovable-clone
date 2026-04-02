package com.project.lovable_clone.mapper;

import com.project.lovable_clone.dto.member.MemberResponse;
import com.project.lovable_clone.entity.ProjectMember;
import com.project.lovable_clone.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {

    @Mapping(source = "id",target = "userId")
    MemberResponse toMemberResponseFromUser(User user);

    @Mapping(source = "user.id",target = "userId")
    @Mapping(source = "user.email",target = "email")
    @Mapping(source = "user.name",target = "name")
    @Mapping(source = "user.avatarUrl",target = "avatarUrl")
    MemberResponse toMemberResponseFromProjectMember(ProjectMember projectMember);
}
