package com.gregori.member.service;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gregori.common.exception.DuplicateException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.ValidationException;
import com.gregori.member.domain.Member;
import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.dto.MemberPasswordUpdateDto;
import com.gregori.member.mapper.MemberMapper;

import static com.gregori.member.domain.Member.Status.DEACTIVATE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

	@Mock
	private MemberMapper memberMapper;
	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private MemberServiceImpl memberService;

	@Test
	@DisplayName("이메일이 중복되지 않으면 새로운 회원을 DB에 저장하고 id를 반환한다.")
	 void should_register_when_notDuplicatedEmail() {

		// given
		final MemberRegisterDto dto = new MemberRegisterDto("name", "email", "password");

		given(memberMapper.findByEmail("email")).willReturn(Optional.empty());
		given(passwordEncoder.encode(dto.getPassword())).willReturn("encodedPassword");

		// when
		memberService.register(dto);

		// then
		verify(memberMapper).insert(any(Member.class));
	}

	@Test
	@DisplayName("회원 이메일이 중복되면 가입을 실패한다.")
	void should_DuplicationException_when_duplicationEmail() {

		// given
		final MemberRegisterDto dto = new MemberRegisterDto("name", "email", "password");

		given(memberMapper.findByEmail("email")).willReturn(Optional.of(new Member("name", "email", "password")));

		// when, then
		assertThrows(DuplicateException.class, () -> memberService.register(dto));
	}

	@Test
	@DisplayName("DB에 회원이 존재하면 회원 이름을 수정한다.")
	void should_updateMemberName_when_existMember() {

		// given
		Long id = 1L;
		String name = "name";

		given(memberMapper.findById(1L)).willReturn(Optional.of(new Member()));

		// when
		memberService.updateMemberName(id, name);

		// then
		verify(memberMapper).updateName(id, name);
	}

	@Test
	@DisplayName("DB에 회원이 존재하지 않으면 회원 이름 수정을 실패한다.")
	void should_NotFoundException_when_nonExistMemberUpdateName() {

		// given
		Long id = 1L;
		String name = "name";

		given(memberMapper.findById(1L)).willReturn(Optional.empty());

		// when, then
		assertThrows(NotFoundException.class, () -> memberService.updateMemberName(id, name));
	}

	@Test
	@DisplayName("DB에 회원이 존재하고 기존 비밀번호가 일치하면 회원 비밀번호를 수정한다.")
	void should_updateMemberPassword_when_existMember() {

		// given
		Member member = new Member("name", "email", passwordEncoder.encode("password"));
		MemberPasswordUpdateDto dto = new MemberPasswordUpdateDto(1L, "password", "newPassword");

		given(memberMapper.findById(1L)).willReturn(Optional.of(member));

		// when
		memberService.updateMemberPassword(dto);

		// then
		verify(memberMapper).updatePassword(any(), any());
	}

	@Test
	@DisplayName("DB에 회원이 존재하지 않으면 회원 비밀번호 변경을 실패한다.")
	void should_NotFoundException_when_nonExistMemberUpdatePassword() {

		// given
		MemberPasswordUpdateDto dto = new MemberPasswordUpdateDto(1L, "password", "newPassword");

		given(memberMapper.findById(1L)).willReturn(Optional.empty());

		// when, then
		assertThrows(NotFoundException.class, () -> memberService.updateMemberPassword(dto));
	}

	@Test
	@DisplayName("DB의 기존 비밀번호가 일치하지 않으면 회원 비밀번호 변경을 실패한다.")
	void should_ValidationException_when_nonExistMemberUpdatePassword() {

		// given
		Member member = new Member("name", "email", "aa11111!");
		MemberPasswordUpdateDto dto = new MemberPasswordUpdateDto(1L, "password", "newPassword");

		given(memberMapper.findById(1L)).willReturn(Optional.of(member));

		// when, then
		assertThrows(ValidationException.class, () -> memberService.updateMemberPassword(dto));
	}

	@Test
	@DisplayName("DB에 회원이 존재하면 회원을 삭제하고 id를 반환한다.")
	void should_deleteMember_when_existMember() {

		// given
		final Long memberId = 1L;
		final Member member = new Member("name", "email", "password");
		given(memberMapper.findById(memberId)).willReturn(Optional.of(member));

		// when
		memberService.deleteMember(memberId);

		// then
		verify(memberMapper).updateStatus(memberId, DEACTIVATE);
	}

	@Test
	@DisplayName("DB에 회원이 존재하지 않으면 회원 정보 삭제를 실패한다.")
	void should_NotFoundException_when_nonExistMemberDelete() {

		// given
		given(memberMapper.findById(1L)).willReturn(Optional.empty());

		// when, then
		assertThrows(NotFoundException.class, () -> memberService.deleteMember(1L));
	}

	@Test
	@DisplayName("DB에 회원이 존재하면 회원을 조회해서 반환한다.")
	void getMember() {

		// given
		final Long memberId = 1L;
		final Member member = new Member("name", "email", "password");
		given(memberMapper.findById(1L)).willReturn(Optional.of(member));

		// when
		memberService.getMember(memberId);

		// then
		verify(memberMapper).findById(memberId);
	}

	@Test
	@DisplayName("DB에 회원이 존재하지 않으면 회원 정보 조회를 실패한다.")
	void should_NotFoundException_when_nonExistMemberGet() {

		// given
		given(memberMapper.findById(1L)).willReturn(Optional.empty());

		// when, then
		assertThrows(NotFoundException.class, () -> memberService.getMember(1L));
	}
}
