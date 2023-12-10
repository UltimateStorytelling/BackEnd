package com.ultimatestorytelling.backend.common.service;

import com.ultimatestorytelling.backend.jwt.TokenProvider;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    // 관리자 권한 확인
    public boolean isAdmin(String accessToken) {
        Authentication authentication = tokenProvider.validateAndGetAuthentication(accessToken);
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
    }

    // 사용자 인증 및 Member 객체 조회
    public Member getAuthenticatedMember(String accessToken) {
        Authentication authentication = tokenProvider.validateAndGetAuthentication(accessToken);
        String userEmail = authentication.getName();
        return memberRepository.findMemberByMemberEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
    }
}
