package com.project.lovable_clone.mapper;

import com.project.lovable_clone.dto.project.ProjectResponse;
import com.project.lovable_clone.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

   ProjectResponse toProjectResponse(Project project);

}
