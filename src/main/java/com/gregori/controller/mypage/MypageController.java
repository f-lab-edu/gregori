package com.gregori.controller.mypage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.domain.member.Member;
import com.gregori.service.member.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

	private final MemberService memberService;

	@PostMapping("/member-info/{memberId}")
	public Long updateMember(Long memberId, Member updateMember) {
		return memberService.updateMember(memberId, updateMember);
	}

	@DeleteMapping("/member-info/{memberId}")
	public Long deleteMember(Long memberId) {
		return memberService.deleteMember(memberId);
	}

	@GetMapping("/member-info")
	public Member findMemberById(Long memberId) {
		return memberService.findMemberById(memberId);
	}
}
