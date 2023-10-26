package com.ultimatestorytelling.backend.member.command.application.service;

import com.ultimatestorytelling.backend.jwt.TokenProvider;
import com.ultimatestorytelling.backend.member.command.application.dto.sign.SignRequestDTO;
import com.ultimatestorytelling.backend.member.command.application.dto.update.UpdateInfoRequestDTO;
import com.ultimatestorytelling.backend.member.command.application.dto.update.UpdatePwdRequestDTO;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.enumvalue.MemberRole;
import com.ultimatestorytelling.backend.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    //회원가입
    @Transactional
    public void register(SignRequestDTO requestDTO) throws Exception{
        try{
            //회원가입과정
            Member member = Member.builder()
                    .memberEmail(requestDTO.getMemberEmail())
                    .memberPwd(passwordEncoder.encode(requestDTO.getMemberPwd()))
                    .memberNickname(requestDTO.getMemberNickname())
                    .memberIsDeleted(false)
                    .memberRole(MemberRole.MEMBER)
                    .reportCount(0)
                    .build();

            memberRepository.save(member);
        } catch (Exception e){
            throw new Exception("잘못된 요청입니다.");
        }
    }

    //닉네임 중복조회
    @Transactional(readOnly = true)
    public void checkDuplicateMemberNickname(String memberNickname) {

        if (memberNickname == null || memberNickname.trim().isEmpty()) {
            throw new IllegalArgumentException("닉네임이 유효하지 않습니다.");
        } else if(memberRepository.existsByMemberNickname(memberNickname)){
            throw new IllegalArgumentException("사용중인 닉네임 입니다.");
        }
    }

    //아이디 중복조회
    @Transactional(readOnly = true)
    public void checkDuplicateMemberEmail(String memberEmail) {

        if (memberEmail == null || memberEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("이메일이 유효하지 않습니다.");
        }else if(memberRepository.existsByMemberEmail(memberEmail)){
            throw new IllegalArgumentException("사용중인 이메일 입니다.");
        }
    }

    //회원탈퇴
    @Transactional
    public void deleteMember(Long memberNo, String accessToken) {

        // 토큰의 유효성 검사
        if (!tokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        // accessToken을 사용하여 사용자를 인증하고 해당 사용자의 정보를 가져옵니다.
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        String userEmail = authentication.getName();
        Member member = memberRepository.findMemberByMemberNo(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // 권한 확인
        if (!member.getMemberEmail().equals(userEmail)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        member.deleteMember(null, true, "탈퇴한 사용자 입니다.");
    }

    //비밀번호변경
    @Transactional
    public void changeMemberPwd(UpdatePwdRequestDTO requestDTO, Long memberNo, String accessToken) {

        // 토큰의 유효성 검사
        if (!tokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        // accessToken을 사용하여 사용자를 인증하고 해당 사용자의 정보를 가져옵니다.
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        String userEmail = authentication.getName();
        Member member = memberRepository.findMemberByMemberNo(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // 권한 확인
        if (!member.getMemberEmail().equals(userEmail)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        if (!passwordEncoder.matches(requestDTO.getMemberPwd(), member.getMemberPwd())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        } else if (passwordEncoder.matches(requestDTO.getCurrentPassword(), member.getMemberPwd())) {
            throw new IllegalArgumentException("같은 비밀번호로는 변경할 수 없습니다.");
        }

        member.setMemberPwd(passwordEncoder.encode(requestDTO.getCurrentPassword()));
    }

    //회원정보변경
    // 회원 정보 변경
    @Transactional
    public void updateMemberInfo(UpdateInfoRequestDTO requestDTO, Long memberNo, String accessToken) {

        // 토큰의 유효성 검사
        if (!tokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        // accessToken을 사용하여 사용자를 인증하고 해당 사용자의 정보를 가져옵니다.
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        String userEmail = authentication.getName();
        Member member = memberRepository.findMemberByMemberNo(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // 권한 확인
        if (!member.getMemberEmail().equals(userEmail)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        member.setMemberNickname(requestDTO.getMemberNickname());
    }
}
