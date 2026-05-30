package com.application.lovable_clone.repository;

import com.application.lovable_clone.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    @Query(
            """
    SELECT p from Project p
    where p.deletedAt IS NULL
    AND EXISTS
    (
       SELECT 1 FROM ProjectMember pm
       WHERE pm.id.userId=:userId
       and pm.id.projectId=p.id
    )
    ORDER BY p.updatedAt DESC
"""
    )
    List<Project> findAllAccessibleByUser(@Param("userId") Long userId);

    @Query("""
      SELECT p from Project p
      WHERE p.deletedAt IS NULL
      AND EXISTS(
      SELECT 1 FROM ProjectMember pm
      WHERE pm.id.projectId=:projectId
      AND pm.id.userId=:userId
      )
      AND p.id=:projectId
""")
    Optional<Project> findAccessibleProjectById(
            @Param("projectId") Long projectId,
            @Param("userId") Long userId);
}
