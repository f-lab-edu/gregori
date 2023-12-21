package com.gregori.member.service;

import java.util.List;

import com.gregori.common.exception.BusinessRuleViolationException;
import com.gregori.common.exception.DuplicateException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.ValidationException;
import com.gregori.member.domain.Member;
import com.gregori.member.dto.MemberNameUpdateDto;
import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberPasswordUpdateDto;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.order.domain.Order;
import com.gregori.order.mapper.OrderMapper;
import com.gregori.refresh_token.mapper.RefreshTokenMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.mapper.SellerMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gregori.auth.domain.Authority.SELLING_MEMBER;
import static com.gregori.member.domain.Member.Status.DEACTIVATE;
import static com.gregori.order.domain.Order.Status.ORDER_PROCESSING;
import static com.gregori.seller.domain.Seller.Status.CLOSED;
import static com.gregori.seller.domain.Seller.Status.OPERATING;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;

    private final MemberMapper memberMapper;
    private final SellerMapper sellerMapper;
    private final OrderMapper orderMapper;
    private final RefreshTokenMapper refreshTokenMapper;

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
    public void deleteMember(Long memberId) {

        Member member = memberMapper.findById(memberId).orElseThrow(NotFoundException::new);

        List<Order> orders = orderMapper.findByMemberId(memberId).stream()
            .filter(order -> order.getStatus() == ORDER_PROCESSING)
            .toList();

        if (!orders.isEmpty()) {
            throw new BusinessRuleViolationException("진행 중인 주문이 있으면 탈퇴 신청이 불가합니다.");
        }

        if (member.getAuthority() == SELLING_MEMBER) {
            List<Seller> sellers = sellerMapper.findByMemberId(memberId).stream()
                .filter(seller -> seller.getStatus() == OPERATING)
                .toList();

            if (!sellers.isEmpty()) {
                throw new BusinessRuleViolationException("사업장을 전부 폐업하지 않으면 탈퇴 신청이 불가합니다.");
            }
        }

        memberMapper.updateStatus(memberId, DEACTIVATE);
        refreshTokenMapper.findByRefreshTokenKey(memberId.toString())
            .ifPresent(token -> refreshTokenMapper.deleteById(token.getId()));
    }

    @Override
    public MemberResponseDto getMember(Long memberId) {

        Member member = memberMapper.findById(memberId).orElseThrow(NotFoundException::new);

        return new MemberResponseDto().toEntity(member);
    }
}
