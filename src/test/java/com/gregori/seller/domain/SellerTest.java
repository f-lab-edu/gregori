package com.gregori.seller.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.common.domain.IsDeleted;
import com.gregori.member.domain.Member;

import static com.gregori.common.domain.IsDeleted.FALSE;
import static com.gregori.common.domain.IsDeleted.TRUE;
import static org.assertj.core.api.Assertions.assertThat;

class SellerTest {

	@Test
	@DisplayName("Seller 객체의 필드를 수정한다.")
	void should_updateSellerInfo() {

		// given
		Seller seller = new Seller(1L, "number", "name");

		// when
		seller.updateSellerInfo("newNumber", "newName");

		// then
		assertThat(seller.getBusinessNumber()).isEqualTo("newNumber");
		assertThat(seller.getBusinessName()).isEqualTo("newName");
	}


	@Test
	@DisplayName("Seller 객체의 삭제 여부를 'TRUE'로 변경한다.")
	void should_isDeletedTrue() {

		// given
		Seller seller = new Seller(1L, "number", "name");
		IsDeleted isDeleted = seller.getIsDeleted();

		// when
		seller.isDeletedTrue();
		IsDeleted result = seller.getIsDeleted();

		// then
		assertThat(isDeleted).isEqualTo(FALSE);
		assertThat(result).isEqualTo(TRUE);
	}
}
