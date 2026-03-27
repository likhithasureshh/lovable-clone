package com.project.lovable_clone.controller;

import com.project.lovable_clone.dto.member.InviteMemberRequest;
import com.project.lovable_clone.dto.member.MemberResponse;
import com.project.lovable_clone.dto.member.UpdateMemberRoleRequest;
import com.project.lovable_clone.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/members")
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;
    @GetMapping
    public ResponseEntity<MemberResponse> getProjectMembers(@PathVariable Long projectId)
    {
        Long userId = 1L;
        return ResponseEntity.ok(projectMemberService.getProjectMembers(projectId,userId));
    }

    @PostMapping
    public ResponseEntity<MemberResponse> inviteMember(
            @PathVariable Long projectId,
            @RequestBody InviteMemberRequest request
            )
    {
       Long userId = 1L;
       return ResponseEntity.ok(projectMemberService.inviteMember(projectId,request,userId));
    }

    @PatchMapping(path = "/{memberId}")
    public ResponseEntity<MemberResponse> updatedMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long memberId,
            @RequestBody UpdateMemberRoleRequest request
            )
    {
       Long userId = 1L;
       return ResponseEntity.ok(projectMemberService.updateMemberRole(projectId,request,userId));
    }

    @DeleteMapping(path = "/{memberId}")
    public ResponseEntity<MemberResponse> deleteProjectMember(
            @PathVariable Long projectId,
            @PathVariable Long memberId
    )
    {
        Long userId = 1L;
        return ResponseEntity.ok(projectMemberService.deleteProjectMember(projectId,memberId));
    }
}
