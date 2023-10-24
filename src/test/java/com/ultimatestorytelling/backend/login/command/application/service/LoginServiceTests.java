package com.ultimatestorytelling.backend.login.command.application.service;

import com.ultimatestorytelling.backend.jwt.TokenProvider;
import com.ultimatestorytelling.backend.login.command.application.dto.login.LoginRequestDTO;
import com.ultimatestorytelling.backend.member.command.application.dto.sign.SignRequestDTO;
import com.ultimatestorytelling.backend.member.command.application.service.MemberService;
import com.ultimatestorytelling.backend.member.command.domain.repository.AuthorityRepository;
import com.ultimatestorytelling.backend.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class LoginServiceTests {

    private final MemberService memberService;
    private final LoginService loginService;

    @Autowired
    public LoginServiceTests(MemberService memberService, LoginService loginService){
        this.memberService = memberService;
        this.loginService = loginService;
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
        Map<String, Object> result = loginService.login(loginRequestDTO);

        //then


    }

    @Test
    void renewAccessToken() {
    }
}