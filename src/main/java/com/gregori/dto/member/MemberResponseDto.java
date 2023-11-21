package com.gregori.dto.member;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDto {
	private Long id;
	private String email;
	private String name;
	private String status;
	private ZonedDateTime createdAt;

	@Builder
	public MemberResponseDto(Long id, String email, String name, String status, ZonedDateTime createdAt) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.status = status;
		this.createdAt = createdAt;
	}
}
