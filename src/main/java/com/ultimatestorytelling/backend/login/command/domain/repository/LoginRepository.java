package com.ultimatestorytelling.backend.login.command.domain.repository;

import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberEmail(String memberEmail); //아이디를 기준으로 회원정보 조회
}
