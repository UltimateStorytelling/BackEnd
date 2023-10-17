package com.ultimatestorytelling.backend.member.command.domain.repository;

import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByMemberNo(Long memberNo); //회원번호로 회원정보 조회
    Optional<Member> findMemberByMemberEmail(String memberEmail); // 이메일로 회원정보 조회
    boolean existsByMemberEmail(String memberEmail); //이메일 중복조회
    boolean existsByMemberNickname(String memberNickname); //닉네임 중복조회
    boolean deleteByMemberNo(Long memberNo); //회원 삭제
}
