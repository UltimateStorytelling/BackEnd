package com.ultimatestorytelling.backend.login.command.application.service;

import com.ultimatestorytelling.backend.jwt.TokenProvider;
import com.ultimatestorytelling.backend.login.command.application.dto.login.LoginRequestDTO;
import com.ultimatestorytelling.backend.login.command.application.dto.login.LoginResponseDTO;
import com.ultimatestorytelling.backend.member.command.application.dto.sign.SignRequestDTO;
import com.ultimatestorytelling.backend.member.command.application.dto.update.UpdatePwdRequestDTO;
import com.ultimatestorytelling.backend.member.command.application.service.MemberService;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Authority;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.enumvalue.MemberSocialLogin;
import com.ultimatestorytelling.backend.member.command.domain.repository.AuthorityRepository;
import com.ultimatestorytelling.backend.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LoginServiceTests {

    private final MemberService memberService;
    private final LoginService loginService;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public LoginServiceTests(MemberService memberService, LoginService loginService,
                             TokenProvider tokenProvider, MemberRepository memberRepository,
                             AuthorityRepository authorityRepository){
        this.memberService = memberService;
        this.loginService = loginService;
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
        this.authorityRepository = authorityRepository;
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        //given
        SignRequestDTO memberDTO1 = SignRequestDTO.builder()
                .memberEmail("test1@test.com")
                .memberPwd("testtest123!")
                .memberNickname("testtest1")
                .build();

        memberService.register(memberDTO1);

        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .memberEmail("test1@test.com")
                .memberPwd("testtest123!")
                .build();
        //when
        Map<String, Object> resultMap = loginService.login(loginRequestDTO);

        //then
        LoginResponseDTO loginResponse = (LoginResponseDTO) resultMap.get("loginResponse");
        String memberNickname = loginResponse.getMemberNickname();

        assertEquals(memberNickname,"testtest1");
    }

    @Test
    @DisplayName("액세스토큰 재발행")
    void renewAccessToken() throws Exception {
        //given
        SignRequestDTO memberDTO1 = SignRequestDTO.builder()
                .memberEmail("test1@test.com")
                .memberPwd("testtest123!")
                .memberNickname("testtest1")
                .build();

        memberService.register(memberDTO1);

        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .memberEmail("test1@test.com")
                .memberPwd("testtest123!")
                .build();

        //when
        Map<String, Object> loginResultMap = loginService.login(loginRequestDTO);
        String accessToken = (String)loginResultMap.get("accessToken");
        String refreshToken = (String)loginResultMap.get("refreshToken");

        //then
        Map<String, Object> resultMap = loginService.renewAccessToken(refreshToken);

        assertNotEquals(accessToken, resultMap.get("accessToken"));

    }
}