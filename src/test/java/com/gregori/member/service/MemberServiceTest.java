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
import com.gregori.seller.domain.Seller;
import com.gregori.seller.mapper.SellerMapper;

import static com.gregori.common.domain.IsDeleted.TRUE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private MemberMapper memberMapper;
	@Mock
	private SellerMapper sellerMapper;
	@Mock
	private OrderMapper orderMapper;

	@InjectMocks
	private MemberService memberService;

	@Test
	@DisplayName("회원가입을 성공하면 id를 반환한다.")
	 void should_returnId_when_registerSuccess() {

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
	@DisplayName("회원 이름 갱신을 성공한다.")
	void should_updateMemberName() {

		// given
		Long memberId = 1L;
		String name = "name";

		given(memberMapper.findById(memberId)).willReturn(Optional.of(new Member()));

		// when
		memberService.updateMemberName(memberId, name);

		// then
		verify(memberMapper).updateName(memberId, name);
	}

	@Test
	@DisplayName("회원 비밀번호를 갱신한다.")
	void should_updateMemberPasswordSuccess() {

		// given
		Long memberId = 1L;
		Member member = new Member("name", "email", passwordEncoder.encode("password"));
		MemberPasswordUpdateDto dto = new MemberPasswordUpdateDto("password", "newPassword");

		given(memberMapper.findById(1L)).willReturn(Optional.of(member));

		// when
		memberService.updateMemberPassword(memberId, dto);

		// then
		verify(memberMapper).updatePassword(any(), any());
	}

	@Test
	@DisplayName("올바르지 않은 비밀번호면 비밀번호 갱신을 실패한다.")
	void should_ValidationException_when_incorrectPassword() {

		// given
		Member member = new Member("name", "email", "aa11111!");
		MemberPasswordUpdateDto dto = new MemberPasswordUpdateDto("password", "newPassword");

		given(memberMapper.findById(1L)).willReturn(Optional.of(member));

		// when, then
		assertThrows(ValidationException.class, () -> memberService.updateMemberPassword(1L, dto));
	}

	@Test
	@DisplayName("일반 회원을 탈퇴 처리한다.")
	void should_deleteGeneralMember_when_deleteMemberSuccess() {

		// given
		final Long memberId = 1L;
		final Member member = new Member("name", "email", "password");

		given(memberMapper.findById(memberId)).willReturn(Optional.of(member));
		given(orderMapper.findByMemberId(memberId, null, null)).willReturn(List.of());

		// when
		memberService.deleteMember(memberId);

		// then
		verify(memberMapper).updateIsDeleted(memberId, TRUE);
	}

	@Test
	@DisplayName("판매자 회원을 탈퇴 처리한다.")
	void should_deleteSellingMember_when_deleteMemberSuccess() {

		// given
		final Long memberId = 1L;
		Member member = new Member("name", "email", "password");
		member.sellingMember();

		Order order1 = new Order(1L, "method", 1L, 1L);
		Order order2 = new Order(1L, "method", 1L, 1L);
		order1.orderCanceled();
		order2.orderCompleted();

		given(memberMapper.findById(memberId)).willReturn(Optional.of(member));
		given(orderMapper.findByMemberId(memberId, null, null)).willReturn(List.of(order1, order2));
		given(sellerMapper.findByMemberId(1L, null, null)).willReturn(List.of());

		// when
		memberService.deleteMember(memberId);

		// then
		verify(memberMapper).updateIsDeleted(memberId, TRUE);
	}

	@Test
	@DisplayName("일반 회원의 주문이 진행 중이면 회원 정보 삭제를 실패한다.")
	void should_BusinessRuleViolationException_when_processingOrderExist() {

		// given
		final Member member = new Member("name", "email", "password");
		Order order = new Order(1L, "method", 1L, 1L);

		given(memberMapper.findById(1L)).willReturn(Optional.of(member));
		given(orderMapper.findByMemberId(1L, null, null)).willReturn(List.of(order));

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
		given(orderMapper.findByMemberId(1L, null, null)).willReturn(List.of(order));

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
		given(orderMapper.findByMemberId(1L, null, null)).willReturn(List.of());
		given(sellerMapper.findByMemberId(1L, null, null)).willReturn(List.of(seller));

		// when, then
		assertThrows(BusinessRuleViolationException.class, () -> memberService.deleteMember(1L));
	}

	@Test
	@DisplayName("회원 조회를 성공하면 회원을 반환한다.")
	void should_returnMember_when_getMemberSuccess() {

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
	@DisplayName("회원 조회를 실패하면 에러가 발생한다.")
	void should_NotFoundException_when_findMemberFailure() {

		// given
		MemberNameUpdateDto dto1 = new MemberNameUpdateDto("이름");
		MemberPasswordUpdateDto dto2 = new MemberPasswordUpdateDto("password", "newPassword");

		given(memberMapper.findById(1L)).willReturn(Optional.empty());

		// when, then
		assertThrows(NotFoundException.class, () -> memberService.updateMemberName(1L, "name"));
		assertThrows(NotFoundException.class, () -> memberService.updateMemberPassword(1L, dto2));
		assertThrows(NotFoundException.class, () -> memberService.deleteMember(1L));
		assertThrows(NotFoundException.class, () -> memberService.getMember(1L));
	}
}
