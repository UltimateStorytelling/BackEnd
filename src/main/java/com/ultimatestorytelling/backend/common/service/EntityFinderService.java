package com.ultimatestorytelling.backend.common.service;

import com.ultimatestorytelling.backend.comments.command.domain.aggregate.entity.Comments;
import com.ultimatestorytelling.backend.comments.command.domain.repository.CommentsRepository;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import com.ultimatestorytelling.backend.novel.command.domain.repository.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntityFinderService {

    private final NovelRepository novelRepository;
    private final CommentsRepository commentsRepository;

    //소설 객체 조회
    public Novel getNovel(Long novelNo) {
        return novelRepository.findByNovelNoAndNovelIsDeletedFalse(novelNo)
                .orElseThrow(() -> new IllegalArgumentException("소설을 찾을 수 없습니다."));
    }

    //댓글 객체 조회
    public Comments getComment(Long commentNo) {
        return commentsRepository.findByCommentNoAndIsDeletedFalse(commentNo)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
    }
}