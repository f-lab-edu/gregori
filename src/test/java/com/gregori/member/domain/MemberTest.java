package com.gregori.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.auth.domain.Authority;
import com.gregori.common.domain.IsDeleted;

import static com.gregori.auth.domain.Authority.GENERAL_MEMBER;
import static com.gregori.auth.domain.Authority.SELLING_MEMBER;
import static com.gregori.common.domain.IsDeleted.FALSE;
import static com.gregori.common.domain.IsDeleted.TRUE;
import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

	@Test
	@DisplayName("Member 객체의 권한을 'SELLING_MEMBER'로 변경한다.")
	void should_deactivate() {

		// given
		Member member = new Member("name", "email", "password");
		Authority authority = member.getAuthority();

		// when
		member.sellingMember();
		Authority result = member.getAuthority();

		// then
		assertThat(authority).isEqualTo(GENERAL_MEMBER);
		assertThat(result).isEqualTo(SELLING_MEMBER);
	}

	@Test
	@DisplayName("Member 객체의 삭제 여부를 'TRUE'로 변경한다.")
	void should_isDeletedTrue() {

		// given
		Member member = new Member("name", "email", "password");
		IsDeleted isDeleted = member.getIsDeleted();

		// when
		member.isDeletedTrue();
		IsDeleted result = member.getIsDeleted();

		// then
		assertThat(isDeleted).isEqualTo(FALSE);
		assertThat(result).isEqualTo(TRUE);
	}
}
