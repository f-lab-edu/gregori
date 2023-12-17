package com.gregori.auth.service;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gregori.common.exception.NotFoundException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

	@Mock
	private MemberMapper memberMapper;

	@InjectMocks
	private CustomUserDetailService userDetailService;

	@Test
	@DisplayName("회원 조회를 성공하면 UserDetail을 반환한다.")
	void should_returnUserDetails_when_foundMember() {

		// given
		Member member = new Member("name", "email", "password");

		given(memberMapper.findByEmail(member.getEmail())).willReturn(Optional.of(member));

		// when
		userDetailService.loadUserByUsername(member.getEmail());

		// then
		verify(memberMapper).findByEmail(member.getEmail());
	}

	@Test
	@DisplayName("회원 조회를 실패하면 NotFoundException이 발생한다.")
	void should_NotFoundException_when_notFoundMember() {

		// given
		Member member = new Member("name", "email", "password");

		given(memberMapper.findByEmail(member.getEmail())).willReturn(Optional.empty());

		// when, then
		assertThrows(NotFoundException.class, () ->
			userDetailService.loadUserByUsername(member.getEmail()));
	}
}
