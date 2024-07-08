package com.example.payment.member;

import com.example.payment.member.dto.request.MemberCreateRequest;
import com.example.payment.member.dto.response.MemberCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberCreateResponse> createMember(@Valid @RequestBody MemberCreateRequest request) {
        MemberCreateResponse response = new MemberCreateResponse(memberService.createMember(request));
        return ResponseEntity.ok(response);
    }

}
