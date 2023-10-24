package com.ultimatestorytelling.backend.member.command.application.service;

import com.ultimatestorytelling.backend.jwt.TokenProvider;
import com.ultimatestorytelling.backend.member.command.application.dto.sign.SignRequestDTO;
import com.ultimatestorytelling.backend.member.command.application.dto.update.UpdateInfoRequestDTO;
import com.ultimatestorytelling.backend.member.command.application.dto.update.UpdatePwdRequestDTO;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Authority;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.enumvalue.MemberSocialLogin;
import com.ultimatestorytelling.backend.member.command.domain.repository.AuthorityRepository;
import com.ultimatestorytelling.backend.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTests {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceTests(MemberService memberService, MemberRepository memberRepository, TokenProvider tokenProvider,
                              AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder){
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    @DisplayName("회원 가입")
    @Transactional
    void register() throws Exception {
        //given
        SignRequestDTO memberDTO1 = SignRequestDTO.builder()
                .memberEmail("test1@test.com")
                .memberPwd("testtest123!")
                .memberNickname("testtest1")
                .build();
        SignRequestDTO memberDTO2 = SignRequestDTO.builder()
                .memberEmail("test2@test.com")
                .memberPwd("testtest123!")
                .memberNickname("testtest2")
                .build();

        memberService.register(memberDTO1);
        memberService.register(memberDTO2);

        //when
        Optional<Member> member1 = memberRepository.findMemberByMemberEmail("test1@test.com");
        Optional<Member> member2 = memberRepository.findMemberByMemberEmail("test2@test.com");

        //then
        assertEquals("test1@test.com",member1.get().getMemberEmail());
        assertTrue(passwordEncoder.matches(memberDTO1.getMemberPwd(), member1.get().getMemberPwd()));
        assertEquals("test2@test.com",member2.get().getMemberEmail());
        assertTrue(passwordEncoder.matches(memberDTO2.getMemberPwd(), member2.get().getMemberPwd()));
    }

    @ParameterizedTest
    @DisplayName("닉네임 중복조회")
    @ValueSource(strings = {"","testtest1","testtest2"})
    @Transactional
    void checkDuplicateMemberNickname(String memberNickname) throws Exception {
        //given
        SignRequestDTO memberDTO1 = SignRequestDTO.builder()
                .memberEmail("test1@test.com")
                .memberPwd("testtest123!")
                .memberNickname("testtest1")
                .build();
        memberService.register(memberDTO1);

        //when
        if (!memberNickname.isEmpty() && !"testtest1".equals(memberNickname)) {
            assertDoesNotThrow(() -> memberService.checkDuplicateMemberNickname(memberNickname));
        } else {
            Throwable throwable = assertThrows(IllegalArgumentException.class, () -> {
                memberService.checkDuplicateMemberNickname(memberNickname);
            });

            //then
            if (memberNickname.isEmpty()) {
                assertEquals("닉네임이 유효하지 않습니다.", throwable.getMessage());
            } else if ("testtest1".equals(memberNickname)) {
                assertEquals("사용중인 닉네임 입니다.", throwable.getMessage());
            }
        }
    }

    @ParameterizedTest
    @DisplayName("이메일 중복조회")
    @ValueSource(strings = {"","test1@test.com","test2@test.com"})
    @Transactional
    void checkDuplicateMemberEmail(String memberEmail) throws Exception {
        //given
        SignRequestDTO memberDTO1 = SignRequestDTO.builder()
                .memberEmail("test1@test.com")
                .memberPwd("testtest123!")
                .memberNickname("testtest1")
                .build();
        memberService.register(memberDTO1);

        //when
        if (!memberEmail.isEmpty() && !"test1@test.com".equals(memberEmail)) {
            assertDoesNotThrow(() -> memberService.checkDuplicateMemberEmail(memberEmail));
        } else {
            Throwable throwable = assertThrows(IllegalArgumentException.class, () -> {
                memberService.checkDuplicateMemberEmail(memberEmail);
            });

            //then
            if (memberEmail.isEmpty()) {
                assertEquals("이메일이 유효하지 않습니다.", throwable.getMessage());
            } else if ("test1@test.com".equals(memberEmail)) {
                assertEquals("사용중인 이메일 입니다.", throwable.getMessage());
            }
        }
    }

    @Test
    @DisplayName("회원 삭제")
    @Transactional
    void deleteMember() throws Exception {
        //given
        SignRequestDTO memberDTO1 = SignRequestDTO.builder()
                .memberEmail("test1@test.com")
                .memberPwd("testtest123!")
                .memberNickname("testtest1")
                .build();
        memberService.register(memberDTO1);

        //회원번호 조회
        Optional<Member> member = memberRepository.findMemberByMemberEmail("test1@test.com");
        Long memberNo = member.get().getMemberNo();

        //엑세스 토큰 발급
        // 멤버 정보를 기반으로 JWT 액세스 토큰 생성
        String accessToken = tokenProvider.createAccessToken(
                new UsernamePasswordAuthenticationToken(
                        member.get().getMemberEmail(),
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(member.get().getMemberRole().name()))
                )
        );
        String refreshToken = tokenProvider.createRefreshToken();

        //토큰을 db에 저장하는 과정
        Authority authority = authorityRepository.findByMember(member.get())
                .orElseGet(() -> Authority.builder()
                        .member(member.get())
                        .build());
        authority.updateToken("Bearer", accessToken, refreshToken, MemberSocialLogin.LOCAL);

        // when
        memberService.deleteMember(memberNo, authority.getAccessToken());

        // then
        assertTrue(member.get().getMemberIsDeleted());
    }

    @Test
    @DisplayName("회원 비밀번호 변경")
    void changeMemberPwd() throws Exception {
        //given
        SignRequestDTO memberDTO1 = SignRequestDTO.builder()
                .memberEmail("test1@test.com")
                .memberPwd("testtest123!")
                .memberNickname("testtest1")
                .build();
        memberService.register(memberDTO1);
        UpdatePwdRequestDTO updatePwdRequestDTO1 = UpdatePwdRequestDTO.builder()
                .currentPassword("testtest1234!")
                .memberPwd("testtest123!")
                .build();

        //회원번호 조회
        Optional<Member> member = memberRepository.findMemberByMemberEmail("test1@test.com");
        Long memberNo = member.get().getMemberNo();

        //엑세스 토큰 발급
        // 멤버 정보를 기반으로 JWT 액세스 토큰 생성
        String accessToken = tokenProvider.createAccessToken(
                new UsernamePasswordAuthenticationToken(
                        member.get().getMemberEmail(),
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(member.get().getMemberRole().name()))
                )
        );
        String refreshToken = tokenProvider.createRefreshToken();

        //토큰을 db에 저장하는 과정
        Authority authority = authorityRepository.findByMember(member.get())
                .orElseGet(() -> Authority.builder()
                        .member(member.get())
                        .build());
        authority.updateToken("Bearer", accessToken, refreshToken, MemberSocialLogin.LOCAL);

        //when
        memberService.changeMemberPwd(updatePwdRequestDTO1, memberNo, authority.getAccessToken());

        //then
        assertTrue(passwordEncoder.matches(updatePwdRequestDTO1.getCurrentPassword(), member.get().getMemberPwd()));

    }

    @Test
    @DisplayName("회원 닉네임 변경")
    void updateMemberInfo() throws Exception {
        //given
        SignRequestDTO memberDTO1 = SignRequestDTO.builder()
                .memberEmail("test1@test.com")
                .memberPwd("testtest123!")
                .memberNickname("testtest1")
                .build();
        memberService.register(memberDTO1);
        UpdateInfoRequestDTO updateInfoRequestDTO1 = UpdateInfoRequestDTO.builder()
                .memberNickname("testtest2")
                .build();

        //회원번호 조회
        Optional<Member> member = memberRepository.findMemberByMemberEmail("test1@test.com");
        Long memberNo = member.get().getMemberNo();

        //엑세스 토큰 발급
        // 멤버 정보를 기반으로 JWT 액세스 토큰 생성
        String accessToken = tokenProvider.createAccessToken(
                new UsernamePasswordAuthenticationToken(
                        member.get().getMemberEmail(),
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(member.get().getMemberRole().name()))
                )
        );
        String refreshToken = tokenProvider.createRefreshToken();

        //토큰을 db에 저장하는 과정
        Authority authority = authorityRepository.findByMember(member.get())
                .orElseGet(() -> Authority.builder()
                        .member(member.get())
                        .build());
        authority.updateToken("Bearer", accessToken, refreshToken, MemberSocialLogin.LOCAL);

        //when
        memberService.updateMemberInfo(updateInfoRequestDTO1, memberNo, authority.getAccessToken());

        //then
        assertEquals(updateInfoRequestDTO1.getMemberNickname(), member.get().getMemberNickname());

    }
}