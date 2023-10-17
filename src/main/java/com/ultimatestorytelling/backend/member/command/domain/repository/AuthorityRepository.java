package com.ultimatestorytelling.backend.member.command.domain.repository;


import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Authority;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByMember(Member member);

}
