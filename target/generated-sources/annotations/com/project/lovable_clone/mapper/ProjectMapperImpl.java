package com.project.lovable_clone.mapper;

import com.project.lovable_clone.dto.auth.UserProfileResponse;
import com.project.lovable_clone.dto.project.ProjectResponse;
import com.project.lovable_clone.entity.Project;
import com.project.lovable_clone.entity.User;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T00:18:32+0530",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.10 (Oracle Corporation)"
)
@Component
public class ProjectMapperImpl implements ProjectMapper {

    @Override
    public ProjectResponse toProjectResponse(Project project) {
        if ( project == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        UserProfileResponse owner = null;

        id = project.getId();
        name = project.getName();
        createdAt = project.getCreatedAt();
        updatedAt = project.getUpdatedAt();
        owner = userToUserProfileResponse( project.getOwner() );

        ProjectResponse projectResponse = new ProjectResponse( id, name, createdAt, updatedAt, owner );

        return projectResponse;
    }

    protected UserProfileResponse userToUserProfileResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String email = null;
        String name = null;
        String avatarUrl = null;

        id = user.getId();
        email = user.getEmail();
        name = user.getName();
        avatarUrl = user.getAvatarUrl();

        UserProfileResponse userProfileResponse = new UserProfileResponse( id, email, name, avatarUrl );

        return userProfileResponse;
    }
}
