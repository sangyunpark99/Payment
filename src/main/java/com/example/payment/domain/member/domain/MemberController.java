package com.example.payment.domain.member.domain;

import com.example.payment.domain.member.dto.request.MemberCreateRequest;
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

    @PostMapping()
    public ResponseEntity<Long> createMember(@Valid @RequestBody MemberCreateRequest request) {
        Long id = memberService.createMember(request);
        return ResponseEntity.ok(id);
    }

}
