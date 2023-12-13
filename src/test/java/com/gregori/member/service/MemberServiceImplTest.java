package com.gregori.member.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.gregori.common.exception.NotFoundException;
import com.gregori.member.domain.Member;
import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberUpdateDto;
import com.gregori.member.mapper.MemberMapper;

import static com.gregori.member.domain.Member.Status.DEACTIVATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ActiveProfiles("test")
class MemberServiceImplTest {
	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private MemberService memberService;

	List<Long> memberIds = new ArrayList<>();

	@AfterEach
	void afterEach() {
		if (!memberIds.isEmpty()) {
			memberMapper.deleteByIds(memberIds);
			memberIds.clear();
		}
	}


	@Test
	@DisplayName("새로운 회원을 DB에 저장하고 id를 반환한다.")
	 void saveMember() {
		// given
		MemberRegisterDto memberRegisterDto = new MemberRegisterDto("일호", "z@z.z", "aa11111!");

		// when
		MemberResponseDto result = memberService.register(memberRegisterDto);
		Member member = memberMapper.findById(result.getId()).orElseThrow(NotFoundException::new);
		memberIds.add(member.getId());

		// then
		assertEquals(result.getId(), member.getId());
		assertEquals(member.getName(), "일호");
		assertEquals(member.getEmail(), "z@z.z");
	}

	@Test
	@DisplayName("DB에 저장된 회원을 수정하고 id를 반환한다.")
	void updateMember() {
		// given
		Member member = Member.builder()
			.name("일호")
			.email("a@a.a")
			.password("aa11111!")
			.build();
		memberMapper.insert(member);
		memberIds.add(member.getId());

		MemberUpdateDto memberUpdateDto = new MemberUpdateDto(member.getId(),"이호", "bb22222@");

		// when
		Long result = memberService.updateMember(memberUpdateDto);
		Member findMember = memberMapper.findById(result).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result, findMember.getId());
		assertEquals(memberUpdateDto.getName(), findMember.getName());
		assertNotEquals(memberUpdateDto.getPassword(), findMember.getPassword());
	}

	@Test
	@DisplayName("DB에 저장된 회원의 상태를 변경하고 id를 반환한다.")
	void deleteMember() {
		// given
		Member member = Member.builder()
			.name("일호")
			.email("a@a.a")
			.password("aa11111!")
			.build();
		memberMapper.insert(member);
		memberIds.add(member.getId());

		// when
		Long result = memberService.deleteMember(member.getId());
		Member findMember = memberMapper.findById(member.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result, findMember.getId());
		assertEquals(findMember.getStatus(), DEACTIVATE);

	}

	@Test
	@DisplayName("회원 id로 DB에 저장된 회원을 조회해서 반환한다.")
	void getMember() {
		// given
		Member member = Member.builder()
			.name("일호")
			.email("a@a.a")
			.password("aa11111!")
			.build();
		memberMapper.insert(member);
		memberIds.add(member.getId());

		// when
		MemberResponseDto result = memberService.getMember(member.getId());

		// then
		assertEquals(result.getId(), member.getId());
		assertEquals(result.getName(), member.getName());
		assertEquals(result.getStatus(), member.getStatus());
	}
}
