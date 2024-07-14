package com.example.payment.member;

import static com.example.payment.global.error.ErrorCode.CHANE_PASSWORD_SUCCESS;
import static com.example.payment.global.error.ErrorCode.DELETE_MEMBER_SUCCESS;

import com.example.payment.member.dto.MemberDto;
import com.example.payment.member.dto.request.MemberCreateRequest;
import com.example.payment.member.dto.request.MemberDeleteRequest;
import com.example.payment.member.dto.request.PasswordUpdateRequest;
import com.example.payment.member.dto.response.MemberCreateResponse;
import com.example.payment.member.dto.response.MemberDeleteResponse;
import com.example.payment.member.dto.response.PasswordUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberCreateResponse> createMember(@Valid @RequestBody MemberCreateRequest request) {
        MemberCreateResponse response = new MemberCreateResponse(memberService.createMember(request));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> findMember(@PathVariable Long id) {
        MemberDto response = memberService.findMember(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/password")
    public ResponseEntity<PasswordUpdateResponse> updatePassword(@Valid @RequestBody PasswordUpdateRequest request) {
        memberService.changePassword(request);
        return ResponseEntity.ok(new PasswordUpdateResponse(CHANE_PASSWORD_SUCCESS.getMessage()));
    }

    @DeleteMapping
    public ResponseEntity<MemberDeleteResponse> deleteMember(@Valid @RequestBody MemberDeleteRequest request) {
        memberService.deleteMember(request);
        return ResponseEntity.ok(new MemberDeleteResponse(DELETE_MEMBER_SUCCESS.getMessage()));
    }
}