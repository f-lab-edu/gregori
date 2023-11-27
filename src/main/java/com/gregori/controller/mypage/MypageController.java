package com.gregori.controller.mypage;

import static java.lang.Long.*;

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
import com.gregori.common.response.CustomResponse;
import com.gregori.dto.member.MemberUpdateDto;
import com.gregori.service.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/member-info")
public class MypageController {
	private final MemberService memberService;

	@PostMapping
	public ResponseEntity<Object> updateMember(@RequestBody @Valid MemberUpdateDto mypageUpdateDto) {
		AuthorizationCheck(mypageUpdateDto.getId());

		CustomResponse<Object> response = CustomResponse
			.success(memberService.updateMember(mypageUpdateDto), "회원 수정에 성공했습니다.");

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{memberId}")
	public ResponseEntity<Object> deactivateMember(@PathVariable Long memberId) {
		AuthorizationCheck(memberId);

		CustomResponse<Object> response = CustomResponse
			.success(memberService.deactivateMember(memberId), "회원 탈퇴에 성공했습니다.");

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{memberId}")
	public ResponseEntity<Object> findMemberById(@PathVariable Long memberId) {
		AuthorizationCheck(memberId);

		CustomResponse<Object> response = CustomResponse
			.success(memberService.findMemberById(memberId), "회원 정보를 가져왔습니다.");

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	private void AuthorizationCheck(Long memberId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long currentMemberId = parseLong(authentication.getName());
		if (currentMemberId != memberId) {
			throw new AccessDeniedException();
		}
	}
}
