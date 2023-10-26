package com.ultimatestorytelling.backend.login.command.application.service;

import com.ultimatestorytelling.backend.jwt.TokenProvider;
import com.ultimatestorytelling.backend.login.command.application.dto.auth.AuthResponseDTO;
import com.ultimatestorytelling.backend.login.command.application.dto.login.LoginRequestDTO;
import com.ultimatestorytelling.backend.login.command.application.dto.login.LoginResponseDTO;
import com.ultimatestorytelling.backend.login.command.domain.repository.LoginRepository;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Authority;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.enumvalue.MemberSocialLogin;
import com.ultimatestorytelling.backend.member.command.domain.repository.AuthorityRepository;
import com.ultimatestorytelling.backend.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginRepository loginRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    //로그인
    @Transactional
    public Map<String, Object> login(LoginRequestDTO requestDTO) throws Exception {
        Member member = loginRepository.findByMemberEmail(requestDTO.getMemberEmail()).orElseThrow(
                () -> new BadCredentialsException("잘못된 아이디 혹은 비밀번호 입니다.")); // id조회실패시 exception
        if (!passwordEncoder.matches(requestDTO.getMemberPwd(), member.getMemberPwd())) {
            throw new BadCredentialsException("잘못된 아이디 혹은 비밀번호 입니다."); // 비밀번호 미일치시 exception
        }
        // 멤버 정보를 기반으로 JWT 액세스 토큰 생성
        String accessToken = tokenProvider.createAccessToken(
                new UsernamePasswordAuthenticationToken(
                        member.getMemberEmail(),
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(member.getMemberRole().name()))
                )
        );
        String refreshToken = tokenProvider.createRefreshToken(
                new UsernamePasswordAuthenticationToken(
                        member.getMemberEmail(),
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(member.getMemberRole().name()))
                )
        );

        // 처음로그인
        Authority authority = authorityRepository.findByMember(member)
                .orElseGet(() -> Authority.builder()
                        .member(member)
                        .build());

        authority.updateToken("Bearer", accessToken, refreshToken, MemberSocialLogin.LOCAL);
        // 생성된 액세스 토큰을 Authority 엔티티에 저장
        authorityRepository.save(authority);

        AuthResponseDTO authResponse = new AuthResponseDTO(accessToken, MemberSocialLogin.LOCAL);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("loginResponse", LoginResponseDTO.builder()
                .memberNo(member.getMemberNo())
                .memberNickname(member.getMemberNickname())
                .memberImage(member.getMemberImage())
                .memberRole(member.getMemberRole())
                .authority(Collections.singletonList(authResponse))
                .build());
        resultMap.put("refreshToken", refreshToken);

        return resultMap;
    }

    //엑세스토큰 재발행
    @Transactional
    public Map<String, Object> renewAccessToken(String refreshToken) {

        Map<String, Object> resultMap = new HashMap<>();
        String renewAccessToken = tokenProvider.renewAccessTokenUsingRefreshToken(refreshToken);

        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        String memberEmail = authentication.getName();
        Member member = memberRepository.findMemberByMemberEmail(memberEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Authority authority = authorityRepository.findByMember(member)
                .orElseGet(() -> Authority.builder()
                        .member(member)
                        .build());
        authority.updateToken("Bearer", renewAccessToken, refreshToken, MemberSocialLogin.LOCAL);

        resultMap.put("accessToken", renewAccessToken);

        return resultMap;
    }
}
