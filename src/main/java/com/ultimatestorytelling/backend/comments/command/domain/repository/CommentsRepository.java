package com.ultimatestorytelling.backend.comments.command.domain.repository;

import com.ultimatestorytelling.backend.comments.command.domain.aggregate.entity.Comments;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

    //소설번호로 댓글 조회 (삭제된 댓글 제외)
    List<Comments> findByNovelAndIsDeletedFalseOrderByCommentNoAsc(Novel novel);

    // 댓글 번호로 댓글 조회 (삭제된 댓글 제외)
    Optional<Comments> findByCommentNoAndIsDeletedFalse(Long commentNo);

}
