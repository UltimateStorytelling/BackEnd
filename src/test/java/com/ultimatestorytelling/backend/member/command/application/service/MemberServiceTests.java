package com.ultimatestorytelling.backend.member.command.application.service;

import com.ultimatestorytelling.backend.member.command.application.dto.sign.SignRequestDTO;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTests {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceTests(MemberService memberService, MemberRepository memberRepository){
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @Test
    @DisplayName("회원가입")
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
        assertEquals("test2@test.com",member2.get().getMemberEmail());
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
    void deleteMember(Long memberNo, String accessToken) {
        //given
        //when
        //then
    }

    @Test
    void changeMemberPwd() {
        //given
        //when
        //then
    }

    @Test
    void updateMemberInfo() {
        //given
        //when
        //then
    }
}