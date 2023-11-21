package com.gregori.controller.mypage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.dto.member.MemberResponseDto;
import com.gregori.dto.member.MemberUpdateDto;
import com.gregori.service.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

	private final MemberService memberService;

	@PostMapping("/member-info")
	public ResponseEntity<String> updateMember(@RequestBody @Valid MemberUpdateDto mypageUpdateDto) {
		memberService.updateMember(mypageUpdateDto);

		return ResponseEntity.status(HttpStatus.OK).body("회원 수정에 성공했습니다.");
	}

	@DeleteMapping("/member-info/{memberId}")
	public ResponseEntity<String> deactivateMember(@PathVariable Long memberId) {
		memberService.deactivateMember(memberId);

		return ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴에 성공했습니다.");
	}

	@GetMapping("/member-info/{memberId}")
	public ResponseEntity<MemberResponseDto> findMemberById(@PathVariable Long memberId) {
		MemberResponseDto memberResponseDto = memberService.findMemberById(memberId);
		return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
	}
}
