package com.ultimatestorytelling.backend.likes.command.application.service;

import com.ultimatestorytelling.backend.common.service.AuthenticationService;
import com.ultimatestorytelling.backend.common.service.EntityFinderService;
import com.ultimatestorytelling.backend.likes.command.domain.aggregate.entity.Likes;
import com.ultimatestorytelling.backend.likes.command.domain.repository.LikesRepository;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NovelLikesService {

    private final LikesRepository likesRepository;
    private final AuthenticationService authenticationService;
    private final EntityFinderService entityFinderService;

    // 소설에 대한 좋아요 상태 확인
    @Transactional(readOnly = true)
    public boolean checkMyLikeStatusForNovel(Long novelNo, String accessToken) {

        Member member = authenticationService.getAuthenticatedMember(accessToken);

        Novel novel = entityFinderService.getNovel(novelNo);

        return likesRepository.existsByNovelAndMember(novel, member);
    }

    // 소설에 대한 좋아요 토글
    @Transactional
    public void toggleLikeForNovel(Long novelNo, String accessToken) {

        Member member = authenticationService.getAuthenticatedMember(accessToken);

        Novel novel = entityFinderService.getNovel(novelNo);

        Optional<Likes> existingLikeOpt = likesRepository.findByNovelAndMember(novel, member);

        if (existingLikeOpt.isPresent()) {
            // 좋아요 상태를 반전시킵니다.
            Likes existingLike = existingLikeOpt.get();
            existingLike.setLikesStatus(!existingLike.getLikesStatus());
            likesRepository.save(existingLike);
        } else {
            // 처음 좋아요를 누르는 경우, 새로운 좋아요 객체를 생성합니다.
            Likes newLike = Likes.builder()
                    .member(member)
                    .novel(novel)
                    .likesStatus(true) // 처음 상태는 true로 설정
                    .build();
            likesRepository.save(newLike);
        }
    }

    // 소설에 대한 좋아요 갯수 조회
    @Transactional(readOnly = true)
    public int getTotalLikesForNovel(Long novelNo) {

        Novel novel = entityFinderService.getNovel(novelNo);

        return likesRepository.countByNovel(novel);
    }

}
