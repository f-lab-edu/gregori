package com.gregori.mypage.controller;

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

import com.gregori.common.exception.AccessDeniedException;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberUpdateDto;
import com.gregori.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static java.lang.Long.parseLong;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

	private final MemberService memberService;

	@PostMapping
	public ResponseEntity<Long> updateMember(@RequestBody @Valid MemberUpdateDto memberUpdateDto) {

		authorizationCheck(memberUpdateDto.getId());

		memberService.updateMember(memberUpdateDto);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{memberId}")
	public ResponseEntity<Long> deleteMember(@PathVariable Long memberId) {

		authorizationCheck(memberId);

		memberService.deleteMember(memberId);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/{memberId}")
	public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long memberId) {

		authorizationCheck(memberId);

		MemberResponseDto response = memberService.getMember(memberId);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	private void authorizationCheck(Long memberId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		long currentMemberId = parseLong(authentication.getName());

		if (currentMemberId != memberId) {
			throw new AccessDeniedException();
		}
	}
}
