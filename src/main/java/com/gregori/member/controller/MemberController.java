package com.gregori.member.controller;

import static com.gregori.common.response.SuccessMessage.DELETE;
import static com.gregori.common.response.SuccessMessage.GET;
import static com.gregori.common.response.SuccessMessage.REGISTER;
import static com.gregori.common.response.SuccessMessage.UPDATE;
import static java.lang.Long.parseLong;

import com.gregori.common.exception.AccessDeniedException;
import com.gregori.common.response.CustomResponse;
import com.gregori.member.dto.MemberPasswordUpdateDto;
import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberUpdateDto;
import com.gregori.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<CustomResponse<Long>> register(@RequestBody @Valid MemberRegisterDto memberRegisterDto) {

        CustomResponse<Long> response = CustomResponse.success(memberService.register(memberRegisterDto), REGISTER);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/name/{memberId}")
    public ResponseEntity<CustomResponse<Long>> updateMemberName(@PathVariable Long memberId, @RequestBody String name) {

        authorizationCheck(memberId);
        memberService.updateMemberName(memberId, name);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password")
    public ResponseEntity<CustomResponse<Long>> updateMemberPassword(@RequestBody @Valid MemberPasswordUpdateDto dto) {

        authorizationCheck(dto.getId());
        memberService.updateMemberPassword(dto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<CustomResponse<Long>> deleteMember(@PathVariable Long memberId) {

        authorizationCheck(memberId);

        CustomResponse<Long> response = CustomResponse
            .success(memberService.deleteMember(memberId), DELETE);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<CustomResponse<MemberResponseDto>> getMember(@PathVariable Long memberId) {

        authorizationCheck(memberId);

        CustomResponse<MemberResponseDto> response = CustomResponse
            .success(memberService.getMember(memberId), GET);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private void authorizationCheck(Long memberId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long currentMemberId = parseLong(authentication.getName());

        if (currentMemberId != memberId) {
            throw new AccessDeniedException();
        }
    }
}
