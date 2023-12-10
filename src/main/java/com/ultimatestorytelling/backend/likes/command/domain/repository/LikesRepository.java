package com.ultimatestorytelling.backend.likes.command.domain.repository;

import com.ultimatestorytelling.backend.comments.command.domain.aggregate.entity.Comments;
import com.ultimatestorytelling.backend.likes.command.domain.aggregate.entity.Likes;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

        // 소설에 대한 좋아요 관련 메소드

        // 좋아요 상태 확인
        boolean existsByNovelAndMember(Novel novel, Member member);

        // 좋아요 객체 조회
        Optional<Likes> findByNovelAndMember(Novel novel, Member member);

        // 좋아요 수 확인
        int countByNovel(Novel novel);

        // 댓글에 대한 좋아요 관련 메소드

        // 좋아요 상태 확인
        boolean existsByCommentAndMember(Comments comment, Member member);

        // 좋아요 객체 조회
        Optional<Likes> findByCommentAndMember(Comments comment, Member member);

        // 좋아요 수 확인
        int countByComment(Comments comment);
}
