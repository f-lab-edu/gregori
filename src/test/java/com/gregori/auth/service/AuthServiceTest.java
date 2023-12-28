package com.gregori.auth.service;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gregori.auth.dto.AuthSignInDto;
import com.gregori.member.domain.Member;
import com.gregori.member.domain.SessionMember;
import com.gregori.member.mapper.MemberMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private MemberMapper memberMapper;

	@InjectMocks
	private AuthService authService;

	@Test
	@DisplayName("로그인을 성공하면 세션 회원을 반환한다.")
	void should_returnSessionMember_when_signInSuccess() {

		// given
		AuthSignInDto dto = new AuthSignInDto("email", "password");
		Member member = new Member("email", "name", "password");

		given(memberMapper.findByEmail("email")).willReturn(Optional.of(member));
		given(passwordEncoder.matches(dto.getPassword(), member.getPassword())).willReturn(true);

		// when
		SessionMember result = authService.signIn(dto);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getEmail()).isEqualTo(member.getEmail());
	}
}
