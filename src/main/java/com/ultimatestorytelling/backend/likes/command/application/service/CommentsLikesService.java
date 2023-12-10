package com.ultimatestorytelling.backend.likes.command.application.service;

import com.ultimatestorytelling.backend.comments.command.domain.aggregate.entity.Comments;
import com.ultimatestorytelling.backend.common.service.AuthenticationService;
import com.ultimatestorytelling.backend.common.service.EntityFinderService;
import com.ultimatestorytelling.backend.likes.command.domain.aggregate.entity.Likes;
import com.ultimatestorytelling.backend.likes.command.domain.repository.LikesRepository;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentsLikesService {

    private final LikesRepository likesRepository;
    private final AuthenticationService authenticationService;
    private final EntityFinderService entityFinderService;

    // 댓글에 대한 좋아요 상태 확인
    @Transactional(readOnly = true)
    public boolean checkMyLikeStatusForComment(Long commentNo, String accessToken) {

        Member member = authenticationService.getAuthenticatedMember(accessToken);

        Comments comments = entityFinderService.getComment(commentNo);

        return likesRepository.existsByCommentAndMember(comments, member);
    }

    // 댓글에 대한 좋아요 토글
    @Transactional
    public void toggleLikeForComment(Long commentNo, String accessToken) {

        Member member = authenticationService.getAuthenticatedMember(accessToken);

        Comments comments = entityFinderService.getComment(commentNo);

        Optional<Likes> existingLikeOpt = likesRepository.findByCommentAndMember(comments, member);

        if (existingLikeOpt.isPresent()) {
            // 좋아요 상태를 반전시킵니다.
            Likes existingLike = existingLikeOpt.get();
            existingLike.setLikesStatus(!existingLike.getLikesStatus());
            likesRepository.save(existingLike);
        } else {
            // 처음 좋아요를 누르는 경우, 새로운 좋아요 객체를 생성합니다.
            Likes newLike = Likes.builder()
                    .member(member)
                    .comment(comments)
                    .likesStatus(true) // 처음 상태는 true로 설정
                    .build();
            likesRepository.save(newLike);
        }
    }

    // 댓글에 대한 좋아요 갯수 조회
    @Transactional(readOnly = true)
    public int getTotalLikesForComment(Long commentNo) {

        Comments comments = entityFinderService.getComment(commentNo);

        return likesRepository.countByComment(comments);
    }

}
