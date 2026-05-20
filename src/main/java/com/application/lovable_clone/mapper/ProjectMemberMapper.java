package com.application.lovable_clone.mapper;

import com.application.lovable_clone.dto.member.MemberResponse;
import com.application.lovable_clone.entity.ProjectMember;
import com.application.lovable_clone.entity.User;
import com.application.lovable_clone.enums.ProjectRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {
    @Mapping(target = "id",source = "user.id")
    @Mapping(target = "username",source = "user.username")
    @Mapping(target = "name",source = "user.name")
    MemberResponse toMemberResponseFromInvite(ProjectMember projectMember);

    @Mapping(target = "projectRole",constant = "OWNER")
    MemberResponse toMemberResponseFromOwner(User user);
}
