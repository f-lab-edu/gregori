package com.gregori.member.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gregori.common.exception.BusinessRuleViolationException;
import com.gregori.common.exception.DuplicateException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.ValidationException;
import com.gregori.member.domain.Member;
import com.gregori.member.dto.MemberNameUpdateDto;
import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.dto.MemberPasswordUpdateDto;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.order.domain.Order;
import com.gregori.order.mapper.OrderMapper;
import com.gregori.refresh_token.mapper.RefreshTokenMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.mapper.SellerMapper;

import static com.gregori.member.domain.Member.Status.DEACTIVATE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private MemberMapper memberMapper;
	@Mock
	private SellerMapper sellerMapper;
	@Mock
	private OrderMapper orderMapper;
	@Mock
	private RefreshTokenMapper refreshTokenMapper;

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
	@DisplayName("회원 이메일이 중복되면 회원가입을 실패한다.")
	void should_DuplicationException_when_duplicationEmail() {

		// given
		final MemberRegisterDto dto = new MemberRegisterDto("name", "email", "password");

		given(memberMapper.findByEmail("email")).willReturn(Optional.of(new Member("name", "email", "password")));

		// when, then
		assertThrows(DuplicateException.class, () -> memberService.register(dto));
	}

	@Test
	@DisplayName("회원을 찾으면 회원 이름을 수정한다.")
	void should_updateMemberName_when_findMember() {

		// given
		MemberNameUpdateDto dto = new MemberNameUpdateDto(1L, "이름");

		given(memberMapper.findById(1L)).willReturn(Optional.of(new Member()));

		// when
		memberService.updateMemberName(dto);

		// then
		verify(memberMapper).updateName(dto.getId(), dto.getName());
	}

	@Test
	@DisplayName("회원을 찾고 비밀번호가 일치하면 회원 비밀번호를 수정한다.")
	void should_updateMemberPassword_when_findMemberAndCorrectPassword() {

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
	@DisplayName("기존 비밀번호가 일치하지 않으면 회원 비밀번호 변경을 실패한다.")
	void should_ValidationException_when_incorrectPassword() {

		// given
		Member member = new Member("name", "email", "aa11111!");
		MemberPasswordUpdateDto dto = new MemberPasswordUpdateDto(1L, "password", "newPassword");

		given(memberMapper.findById(1L)).willReturn(Optional.of(member));

		// when, then
		assertThrows(ValidationException.class, () -> memberService.updateMemberPassword(dto));
	}

	@Test
	@DisplayName("조건을 만족하면 일반 회원을 탈퇴 처리한다.")
	void should_deleteGeneralMember_when_meetCondition() {

		// given
		final Long memberId = 1L;
		final Member member = new Member("name", "email", "password");

		given(memberMapper.findById(memberId)).willReturn(Optional.of(member));
		given(orderMapper.findByMemberId(memberId)).willReturn(List.of());

		// when
		memberService.deleteMember(memberId);

		// then
		verify(memberMapper).updateStatus(memberId, DEACTIVATE);
		verify(refreshTokenMapper).findByRefreshTokenKey(memberId.toString());
	}

	@Test
	@DisplayName("조건을 만족하면 판매자 회원을 탈퇴 처리한다.")
	void should_deleteSellingMember_when_meetCondition() {

		// given
		final Long memberId = 1L;
		Member member = new Member("name", "email", "password");
		member.sellingMember();

		Order order1 = new Order(1L, "method", 1L, 1L);
		Order order2 = new Order(1L, "method", 1L, 1L);
		order1.orderCancelled();
		order2.orderCompleted();

		Seller seller = new Seller(1L, "123-45-67891", "name");
		seller.closed();

		given(memberMapper.findById(memberId)).willReturn(Optional.of(member));
		given(orderMapper.findByMemberId(memberId)).willReturn(List.of(order1, order2));
		given(sellerMapper.findByMemberId(1L)).willReturn(List.of(seller));

		// when
		memberService.deleteMember(memberId);

		// then
		verify(memberMapper).updateStatus(memberId, DEACTIVATE);
		verify(refreshTokenMapper).findByRefreshTokenKey(memberId.toString());
	}

	@Test
	@DisplayName("일반 회원의 주문이 진행 중이면 회원 정보 삭제를 실패한다.")
	void should_BusinessRuleViolationException_when_processingOrderExist() {

		// given
		final Member member = new Member("name", "email", "password");
		Order order = new Order(1L, "method", 1L, 1L);

		given(memberMapper.findById(1L)).willReturn(Optional.of(member));
		given(orderMapper.findByMemberId(1L)).willReturn(List.of(order));

		// when, then
		assertThrows(BusinessRuleViolationException.class, () -> memberService.deleteMember(1L));
	}

	@Test
	@DisplayName("판매자 회원의 주문이 진행 중이면 회원 정보 삭제를 실패한다.")
	void should_BusinessRuleViolationException_when_processingOrderExist2() {

		// given
		Member member = new Member("name", "email", "password");
		member.sellingMember();
		Order order = new Order(1L, "method", 1L, 1L);

		given(memberMapper.findById(1L)).willReturn(Optional.of(member));
		given(orderMapper.findByMemberId(1L)).willReturn(List.of(order));

		// when, then
		assertThrows(BusinessRuleViolationException.class, () -> memberService.deleteMember(1L));
	}

	@Test
	@DisplayName("판매자 회원의 사업장이 운영중이면 회원 정보 삭제를 실패한다.")
	void should_BusinessRuleViolationException_when_operatingSellerExist() {

		// given
		Member member = new Member("name", "email", "password");
		member.sellingMember();
		Seller seller = new Seller(1L, "123-45-67891", "name");

		given(memberMapper.findById(1L)).willReturn(Optional.of(member));
		given(orderMapper.findByMemberId(1L)).willReturn(List.of());
		given(sellerMapper.findByMemberId(1L)).willReturn(List.of(seller));

		// when, then
		assertThrows(BusinessRuleViolationException.class, () -> memberService.deleteMember(1L));
	}

	@Test
	@DisplayName("회원을 찾으면 회원을 반환한다.")
	void should_getMember_when_findMemberSuccess() {

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
	@DisplayName("회원을 찾지 못하면 에러가 발생한다.")
	void should_NotFoundException_when_findMemberFailure() {

		// given
		MemberNameUpdateDto dto1 = new MemberNameUpdateDto(1L, "이름");
		MemberPasswordUpdateDto dto2 = new MemberPasswordUpdateDto(1L, "password", "newPassword");

		given(memberMapper.findById(1L)).willReturn(Optional.empty());

		// when, then
		assertThrows(NotFoundException.class, () -> memberService.getMember(1L));
		assertThrows(NotFoundException.class, () -> memberService.updateMemberName(dto1));
		assertThrows(NotFoundException.class, () -> memberService.updateMemberPassword(dto2));
		assertThrows(NotFoundException.class, () -> memberService.deleteMember(1L));
	}

}
