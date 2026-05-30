package com.application.lovable_clone.controller;

import com.application.lovable_clone.dto.member.InviteMemberRequest;
import com.application.lovable_clone.dto.member.MemberResponse;
import com.application.lovable_clone.dto.member.UpdateMemberRoleRequest;
import com.application.lovable_clone.security.AuthUtil.AuthUtil;
import com.application.lovable_clone.service.AuthService;
import com.application.lovable_clone.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projects/{projectId}/members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllProjectMembers(@PathVariable Long projectId)
    {

        return ResponseEntity.ok(projectMemberService.getAllProjectMembers(projectId));
    }

    @PostMapping
    public ResponseEntity<MemberResponse> inviteMember(@PathVariable Long projectId, @RequestBody @Valid InviteMemberRequest inviteMemberRequest)
    {

        return ResponseEntity.status(HttpStatus.CREATED).body(projectMemberService.inviteMember(projectId,inviteMemberRequest));
    }

    @PatchMapping(path = "/{memberId}")
    public ResponseEntity<MemberResponse> updateMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long memberId,
            @RequestBody @Valid UpdateMemberRoleRequest request
            )
    {

        return ResponseEntity.ok(projectMemberService.updateMemberRole(projectId,memberId,request));
    }

    @DeleteMapping(path = "/{memberId}")
    public ResponseEntity<Void> removeProjectMember(
            @PathVariable Long projectId,
            @PathVariable Long memberId
    )
    {

        projectMemberService.removeProjectMember(projectId,memberId);
        return ResponseEntity.noContent().build();
    }


}
