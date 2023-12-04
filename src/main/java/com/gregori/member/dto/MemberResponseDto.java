package com.gregori.member.dto;

import java.time.ZonedDateTime;

import com.gregori.member.domain.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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

	public MemberResponseDto toEntity(Member member) {
		return MemberResponseDto.builder()
			.id(member.getId())
			.name(member.getName())
			.email(member.getEmail())
			.status(member.getStatus().toString())
			.createdAt(member.getCreatedAt())
			.build();
	}
}
