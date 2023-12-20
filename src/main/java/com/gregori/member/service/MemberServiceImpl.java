package com.gregori.member.service;

import com.gregori.common.exception.DuplicateException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.ValidationException;
import com.gregori.member.domain.Member;
import com.gregori.member.dto.MemberNameUpdateDto;
import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberPasswordUpdateDto;
import com.gregori.member.mapper.MemberMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gregori.member.domain.Member.Status.DEACTIVATE;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Long register(@Valid MemberRegisterDto dto) throws DuplicateException {

        if (memberMapper.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateException();
        }

        String newPassword = passwordEncoder.encode(dto.getPassword());
        Member member = dto.toEntity(newPassword);
        memberMapper.insert(member);

        return member.getId();
    }

    @Override
    @Transactional
    public void updateMemberName(MemberNameUpdateDto dto) {

        memberMapper.findById(dto.getId()).orElseThrow(NotFoundException::new);
        memberMapper.updateName(dto.getId(), dto.getName());
    }

    @Override
    public void updateMemberPassword(MemberPasswordUpdateDto dto) {

        Member member = memberMapper.findById(dto.getId()).orElseThrow(NotFoundException::new);
        String oldPassword = passwordEncoder.encode(dto.getOldPassword());

        if (!StringUtils.equals(oldPassword, member.getPassword())) {
            throw new ValidationException("기존 비밀번호가 일치하지 않습니다.");
        }

        String newPassword = passwordEncoder.encode(dto.getNewPassword());
        memberMapper.updatePassword(dto.getId(), newPassword);
    }

    @Override
    @Transactional
    public Long deleteMember(Long memberId) {

        memberMapper.findById(memberId).orElseThrow(NotFoundException::new);
        memberMapper.updateStatus(memberId, DEACTIVATE);

        return memberId;
    }

    @Override
    public MemberResponseDto getMember(Long memberId) {

        Member member = memberMapper.findById(memberId).orElseThrow(NotFoundException::new);

        return new MemberResponseDto().toEntity(member);
    }
}
