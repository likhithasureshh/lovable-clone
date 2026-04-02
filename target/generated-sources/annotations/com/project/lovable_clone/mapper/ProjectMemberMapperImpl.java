package com.project.lovable_clone.mapper;

import com.project.lovable_clone.dto.member.MemberResponse;
import com.project.lovable_clone.entity.ProjectMember;
import com.project.lovable_clone.entity.User;
import com.project.lovable_clone.enums.ProjectRole;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-02T20:51:41+0530",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.10 (Oracle Corporation)"
)
@Component
public class ProjectMemberMapperImpl implements ProjectMemberMapper {

    @Override
    public MemberResponse toMemberResponseFromUser(User user) {
        if ( user == null ) {
            return null;
        }

        Long userId = null;
        String email = null;
        String name = null;
        String avatarUrl = null;

        userId = user.getId();
        email = user.getEmail();
        name = user.getName();
        avatarUrl = user.getAvatarUrl();

        ProjectRole role = null;
        Instant invitedAt = null;

        MemberResponse memberResponse = new MemberResponse( userId, email, name, avatarUrl, role, invitedAt );

        return memberResponse;
    }

    @Override
    public MemberResponse toMemberResponseFromProjectMember(ProjectMember projectMember) {
        if ( projectMember == null ) {
            return null;
        }

        Long userId = null;
        String email = null;
        String name = null;
        String avatarUrl = null;
        ProjectRole role = null;
        Instant invitedAt = null;

        userId = projectMemberUserId( projectMember );
        email = projectMemberUserEmail( projectMember );
        name = projectMemberUserName( projectMember );
        avatarUrl = projectMemberUserAvatarUrl( projectMember );
        role = projectMember.getRole();
        invitedAt = projectMember.getInvitedAt();

        MemberResponse memberResponse = new MemberResponse( userId, email, name, avatarUrl, role, invitedAt );

        return memberResponse;
    }

    private Long projectMemberUserId(ProjectMember projectMember) {
        User user = projectMember.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private String projectMemberUserEmail(ProjectMember projectMember) {
        User user = projectMember.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getEmail();
    }

    private String projectMemberUserName(ProjectMember projectMember) {
        User user = projectMember.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getName();
    }

    private String projectMemberUserAvatarUrl(ProjectMember projectMember) {
        User user = projectMember.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getAvatarUrl();
    }
}
