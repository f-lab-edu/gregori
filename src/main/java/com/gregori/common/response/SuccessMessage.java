package com.gregori.common.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
	REGISTER_SUCCESS(HttpStatus.OK, "회원 가입에 성공했습니다."),
	SIGNIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
	SIGNOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공했습니다."),
	UPDATE_MEMBER_SUCCESS(HttpStatus.OK, "회원 수정에 성공했습니다."),
	DELETE_MEMBER_SUCCESS(HttpStatus.OK, "회원 탈퇴에 성공했습니다."),
	GET_MEMBER_SUCCESS(HttpStatus.OK, "회원 정보 찾기에 성공했습니다."),
	GET_ITEM_SUCCESS(HttpStatus.OK, "상품 찾기에 성공했습니다."),
	CREATE_ORDER_SUCCESS(HttpStatus.OK, "주문에 성공했습니다."),
	GET_ORDER_SUCCESS(HttpStatus.OK, "주문 찾기에 성공했습니다.");

	private final HttpStatus httpStatus;
	private final String description;
}
